package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.Direction.NO_MOVEMENT
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.init.shouldRunAnimations
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.ArrayDeque
import java.util.Deque

public class NavigationDelegate(
  private val container: () -> ScreenContainer
) : LifecycleAwareComponent() {

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator

  public val backStack: Deque<NavigationEvent> = ArrayDeque()
  public var currentNavigableSetup: ((NavigableCompat) -> Unit)? = null

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
      showCurrentNavigable(NO_MOVEMENT)
    }
  }

  override fun onDestroy(context: Context) {
    backStack.navigables().forEach { removeFromLifecycle(it) }
    backStack.clear()
    containerView = null
  }

  public fun goTo(
    nextNavigableCompat: NavigableCompat,
    overrideMagellanTransition: MagellanTransition? = null
  ) {
    navigate(FORWARD) { backStack ->
      backStack.push(
        NavigationEvent(
          nextNavigableCompat,
          overrideMagellanTransition ?: getDefaultTransition()
        )
      )
      backStack.peek()!!
    }
  }

  public fun replace(
    nextNavigableCompat: NavigableCompat,
    overrideMagellanTransition: MagellanTransition? = null
  ) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(
        NavigationEvent(
          nextNavigableCompat,
          overrideMagellanTransition ?: getDefaultTransition()
        )
      )
      backStack.peek()!!
    }
  }

  private fun navigateBack() {
    navigate(BACKWARD) { backStack ->
      backStack.pop()
    }
  }

  public fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> NavigationEvent
  ) {
    containerView?.setInterceptTouchEvents(true)
    navigationPropagator.beforeNavigation()
    val from = hideCurrentNavigable(direction)
    val transition = backStackOperation.invoke(backStack).magellanTransition
    val to = showCurrentNavigable(direction)
    navigationPropagator.afterNavigation()
    animateAndRemove(from, to, direction, transition)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    magellanTransition: MagellanTransition
  ) {
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      val transition = if (shouldRunAnimations()) {
        magellanTransition
      } else {
        NoAnimationTransition()
      }
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
    currentNavigableSetup?.invoke(currentNavigable!!)
    attachToLifecycle(
      currentNavigable!!,
      detachedState = when (direction) {
        FORWARD -> LifecycleState.Destroyed
        NO_MOVEMENT, BACKWARD -> currentState.getEarlierOfCurrentState()
      }
    )
    navigationPropagator.showCurrentNavigable(currentNavigable!!)
    when (currentState) {
      is LifecycleState.Shown, is LifecycleState.Resumed -> {
        containerView!!.addView(
          currentNavigable!!.view!!,
          direction.indexToAddView(containerView!!)
        )
      }
      is LifecycleState.Destroyed, is LifecycleState.Created -> {
      }
    }
    return currentNavigable!!.view
  }

  private fun hideCurrentNavigable(direction: Direction): View? {
    return currentNavigable?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(
        currentNavigable,
        detachedState = when (direction) {
          NO_MOVEMENT, FORWARD -> currentState.getEarlierOfCurrentState()
          BACKWARD -> LifecycleState.Destroyed
        }
      )
      navigationPropagator.hideCurrentNavigable(currentNavigable)
      currentView
    }
  }

  public fun goBackTo(navigable: NavigableCompat) {
    navigate(BACKWARD) { backStack ->
      while (navigable != backStack.peek()!!.navigable) {
        backStack.pop()
      }
      backStack.peek()!!
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  public fun goBack(): Boolean {
    return if (!atRoot()) {
      navigateBack()
      true
    } else {
      false
    }
  }

  private fun atRoot() = backStack.size <= 1
}
