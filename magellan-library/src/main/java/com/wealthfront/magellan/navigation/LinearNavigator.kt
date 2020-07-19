package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.Stack

class LinearNavigator(
  private val container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator

  @VisibleForTesting
  override val backStack: Stack<NavigationEvent> = Stack()

  private val currentNavigable: Navigable?
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
      removeFromLifecycle(it, detachedState = Destroyed)
    }
    backStack.clear()
    containerView = null
  }

  fun goTo(nextNavigable: Navigable) {
    navigateTo(nextNavigable, GO)
  }

  fun show(nextNavigable: Navigable) {
    navigateTo(nextNavigable, SHOW)
  }

  fun replaceAndGo(nextNavigable: Navigable) {
    replace(nextNavigable, GO)
  }

  fun replaceAndShow(nextNavigable: Navigable) {
    replace(nextNavigable, SHOW)
  }

  private fun replace(nextNavigable: Navigable, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(NavigationEvent(nextNavigable, navType))
    }
  }

  private fun navigateTo(nextNavigable: Navigable, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      backStack.push(NavigationEvent(nextNavigable, navType))
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
    val to = showCurrentNavigable(direction)
    animateAndRemove(from, to, direction)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction
  ) {
    val navEvent = backStack.peek()!!
    val transition = DefaultTransition()
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      transition.animate(from, to, navEvent.navigationType, direction) {
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
    navigationPropagator.showCurrentNavigable(currentNavigable!!)
    attachToLifecycle(
      currentNavigable!!, detachedState = when (direction) {
      FORWARD -> Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    when (currentState) {
      is Shown, is Resumed -> {
        containerView!!.addView(currentNavigable!!.view!!)
      }
      is Destroyed, is Created -> {}
    }
    return currentNavigable!!.view
  }

  private fun hideCurrentNavigable(direction: Direction): View? {
    return currentNavigable?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(
        currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> Destroyed
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
