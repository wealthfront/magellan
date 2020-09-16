package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition
import com.wealthfront.magellan.transitions.Transition
import com.wealthfront.magellan.view.whenMeasured
import java.util.Stack

class NavigationDelegate(
  private val container: () -> ScreenContainer
) : LifecycleAwareComponent() {

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator

  val backStack: Stack<NavigationEvent> = Stack()
  var currentNavigableSetup: ((NavigableCompat) -> Unit)? = null

  private val currentNavigable: NavigableCompat?
    get() {
      return if (backStack.isNotEmpty()) {
        backStack.peek()?.navigable
      } else {
        null
      }
    }

  private val context: Context?
    get() = currentState.context

  override fun onShow(context: Context) {
    containerView = container()
    currentNavigable?.let {
      containerView!!.addView(it.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    backStack.navigables().forEach {
      removeFromLifecycle(it, detachedState = LifecycleState.Destroyed)
    }
    backStack.clear()
    containerView = null
  }

  fun goTo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigateTo(nextNavigableCompat, overrideTransition ?: DefaultTransition())
  }

  fun show(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigateTo(nextNavigableCompat, overrideTransition ?: ShowTransition())
  }

  fun replaceAndGo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    replace(nextNavigableCompat, overrideTransition ?: DefaultTransition())
  }

  fun replaceAndShow(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    replace(nextNavigableCompat, overrideTransition ?: ShowTransition())
  }

  private fun replace(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(NavigationEvent(nextNavigableCompat, overrideTransition ?: ShowTransition()))
    }
  }

  private fun navigateTo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigate(FORWARD) { backStack ->
      backStack.push(NavigationEvent(nextNavigableCompat, overrideTransition ?: DefaultTransition()))
    }
  }

  private fun navigateBack() {
    navigate(BACKWARD) { backStack ->
      backStack.pop()
    }
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> Unit
  ) {
    containerView?.setInterceptTouchEvents(true)
    val from = hideCurrentNavigable(direction)
    backStackOperation.invoke(backStack)
    val transition = backStack.peek().transition
    val to = showCurrentNavigable(direction)
    animateAndRemove(from, to, direction, transition)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    transition: Transition
  ) {
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      transition.animate(from, to, direction) {
        if (context != null) {
          containerView!!.removeView(from)
          currentNavigable!!.transitionFinished()
          containerView!!.setInterceptTouchEvents(false)
        }
      }
    }
  }

  private fun showCurrentNavigable(direction: Direction): View? {
    navigationPropagator.onNavigate()
    currentNavigableSetup?.invoke(currentNavigable!!)
    navigationPropagator.showCurrentNavigable(currentNavigable!!)
    attachToLifecycle(
      currentNavigable!!, detachedState = when (direction) {
      FORWARD -> LifecycleState.Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    when (currentState) {
      is LifecycleState.Shown, is LifecycleState.Resumed -> {
        containerView!!.addView(currentNavigable!!.view!!)
      }
      is LifecycleState.Destroyed, is LifecycleState.Created -> { }
    }
    return currentNavigable!!.view
  }

  private fun hideCurrentNavigable(direction: Direction): View? {
    return currentNavigable?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(
        currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> LifecycleState.Destroyed
      })
      navigationPropagator.hideCurrentNavigable(currentNavigable)
      currentView
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  fun goBack(): Boolean {
    return if (!atRoot()) {
      navigateBack()
      true
    } else {
      false
    }
  }

  private fun atRoot() = backStack.size <= 1
}
