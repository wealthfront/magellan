package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.init.shouldRunAnimations
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleLimit.CREATED
import com.wealthfront.magellan.lifecycle.LifecycleLimit.NO_LIMIT
import com.wealthfront.magellan.lifecycle.LifecycleLimiter
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.ArrayDeque
import java.util.Deque

public class NavigationDelegate(
  private val container: () -> ScreenContainer
) : LifecycleAwareComponent() {

  public var currentNavigableSetup: ((NavigableCompat<*>) -> Unit)? = null

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator
  public val backStack: Deque<NavigationEvent> = ArrayDeque()

  private val lifecycleLimiter by lifecycle(LifecycleLimiter())

  private val currentNavigable: NavigableCompat<View>?
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
      containerView!!.addView(currentNavigable!!.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    backStack.navigables().forEach { lifecycleLimiter.removeFromLifecycle(it) }
    backStack.clear()
    containerView = null
  }

  public fun goTo(
    nextNavigableCompat: NavigableCompat<View>,
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
    nextNavigableCompat: NavigableCompat<View>,
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
    val from = navigateFrom(currentNavigable)
    val oldBackStack = backStack.map { it.navigable }
    val transition = backStackOperation.invoke(backStack).magellanTransition
    val newBackStack = backStack.map { it.navigable }
    findBackstackChangesAndUpdateStates(oldBackStack = oldBackStack, newBackStack = newBackStack)
    val to = navigateTo(currentNavigable!!, direction)
    navigationPropagator.afterNavigation()
    animateAndRemove(from, to, direction, transition)
  }

  private fun findBackstackChangesAndUpdateStates(
    oldBackStack: List<NavigableCompat<View>>,
    newBackStack: List<NavigableCompat<View>>
  ) {
    val oldNavigables = oldBackStack.toSet()
    val newNavigables = newBackStack.toSet()

    (oldNavigables - newNavigables).forEach { oldNavigable ->
      lifecycleLimiter.removeFromLifecycle(oldNavigable)
    }

    (newNavigables - oldNavigables).forEach { newNavigable ->
      currentNavigableSetup?.invoke(newNavigable)
      lifecycleLimiter.attachToLifecycleWithMaxState(newNavigable, CREATED)
    }
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

  private fun navigateTo(currentNavigable: NavigableCompat<View>, direction: Direction): View? {
    lifecycleLimiter.updateMaxStateForChild(currentNavigable, NO_LIMIT)
    navigationPropagator.onNavigatedTo(currentNavigable)
    when (currentState) {
      is LifecycleState.Shown, is LifecycleState.Resumed -> {
        containerView!!.addView(
          currentNavigable.view!!,
          direction.indexToAddView(containerView!!)
        )
      }
      is LifecycleState.Destroyed, is Created -> {
      }
    }
    return currentNavigable.view
  }

  private fun navigateFrom(currentNavigable: NavigableCompat<View>?): View? {
    return currentNavigable?.let { oldNavigable ->
      val currentView = oldNavigable.view
      lifecycleLimiter.updateMaxStateForChild(oldNavigable, CREATED)
      navigationPropagator.onNavigatedFrom(oldNavigable)
      currentView
    }
  }

  public fun goBackTo(navigable: NavigableCompat<View>) {
    navigate(BACKWARD) { backStack ->
      while (navigable != backStack.peek()!!.navigable) {
        backStack.pop()
      }
      backStack.peek()!!
    }
  }

  public fun resetWithRoot(navigable: NavigableCompat<View>) {
    navigate(FORWARD) { backStack ->
      backStack.clear()
      backStack.push(NavigationEvent(navigable, NoAnimationTransition()))
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

  public fun atRoot(): Boolean = backStack.size <= 1
}
