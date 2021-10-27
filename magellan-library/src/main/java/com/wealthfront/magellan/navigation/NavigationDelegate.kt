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
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.ArrayDeque
import java.util.Deque

public class NavigationDelegate(
  private val container: () -> ScreenContainer
) : LifecycleAwareComponent() {

  public var currentNavigableSetup: ((NavigableCompat) -> Unit)? = null

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator
  public val backStack: Deque<NavigationEvent> = ArrayDeque()

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
    currentNavigable?.let { currentNavigable ->
      containerView!!.addView(currentNavigable.view!!)
      currentNavigable.transitionStarted()
      currentNavigable.transitionFinished()
    }
  }

  override fun onHide(context: Context) {
    containerView = null
  }

  override fun onDestroy(context: Context) {
    backStack.navigables().forEach { lifecycleRegistry.removeFromLifecycle(it) }
    backStack.clear()
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
    oldBackStack: List<NavigableCompat>,
    newBackStack: List<NavigableCompat>
  ) {
    val oldNavigableSet = oldBackStack.toSet()
    val newNavigableSet = newBackStack.toSet()
    if (newNavigableSet.size != newBackStack.size) {
      val numExtraNavigables = newBackStack.size - newNavigableSet.size
      throw IllegalStateException(
        "Cannot have multiple of the same Navigable in the backstack. " +
          "Have $numExtraNavigables extra Navigables in: ${newBackStack.toReadableString()}"
      )
    }

    (oldNavigableSet - newNavigableSet).forEach { oldNavigable ->
      lifecycleRegistry.removeFromLifecycle(oldNavigable)
    }

    (newNavigableSet - oldNavigableSet).forEach { newNavigable ->
      currentNavigableSetup?.invoke(newNavigable)
      lifecycleRegistry.attachToLifecycleWithMaxState(newNavigable, CREATED)
    }
  }

  private fun List<NavigableCompat>.toReadableString() =
    joinToString(prefix = "[", postfix = "]") { it::class.java.simpleName }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    magellanTransition: MagellanTransition
  ) {
    if (to != null) {
      currentNavigable!!.transitionStarted()
    }
    to?.whenMeasured {
      val transition = if (shouldRunAnimations()) {
        magellanTransition
      } else {
        NoAnimationTransition()
      }
      transition.animate(from, to, direction) {
        if (context != null && containerView != null) {
          containerView!!.removeView(from)
          currentNavigable!!.transitionFinished()
          containerView!!.setInterceptTouchEvents(false)
        }
      }
    }
  }

  private fun navigateTo(currentNavigable: NavigableCompat, direction: Direction): View? {
    lifecycleRegistry.updateMaxState(currentNavigable, NO_LIMIT)
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

  private fun navigateFrom(currentNavigable: NavigableCompat?): View? {
    return currentNavigable?.let { oldNavigable ->
      val currentView = oldNavigable.view
      lifecycleRegistry.updateMaxState(oldNavigable, CREATED)
      navigationPropagator.onNavigatedFrom(oldNavigable)
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

  public fun resetWithRoot(navigable: NavigableCompat) {
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
