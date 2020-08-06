package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.Stack

@RestrictTo(LIBRARY_GROUP)
open class LinearNavigator(
  private val container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator

  @VisibleForTesting
  override val backStack: Stack<NavigationItem> = Stack()

  protected val currentNavigable: NavigationItem?
    get() {
      return if (backStack.isNotEmpty()) {
        backStack.peek()
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
    backStack.map { it }.forEach {
      removeFromLifecycle(it, detachedState = Destroyed)
    }
    backStack.clear()
    containerView = null
  }

  fun goTo(nextNavigationItem: NavigationItem) {
    navigateTo(nextNavigationItem, GO)
  }

  fun show(nextNavigationItem: NavigationItem) {
    navigateTo(nextNavigationItem, SHOW)
  }

  fun hide() {
    navigateBack(SHOW)
  }

  fun replaceAndGo(nextNavigationItem: NavigationItem) {
    replace(nextNavigationItem, GO)
  }

  fun replaceAndShow(nextNavigationItem: NavigationItem) {
    replace(nextNavigationItem, SHOW)
  }

  private fun replace(nextNavigationItem: NavigationItem, navType: NavigationType) {
    navigate(FORWARD, navType) { backStack ->
      backStack.pop()
      backStack.push(nextNavigationItem)
    }
  }

  private fun navigateTo(nextNavigationItem: NavigationItem, navType: NavigationType) {
    navigate(FORWARD, navType) { backStack ->
      backStack.push(nextNavigationItem)
    }
  }

  private fun navigateBack(navType: NavigationType) {
    navigate(BACKWARD, navType) { backStack ->
      backStack.pop()
    }
  }

  fun navigate(
    direction: Direction,
    navType: NavigationType,
    backStackOperation: (Stack<NavigationItem>) -> Unit
  ) {
    containerView?.setInterceptTouchEvents(true)
    val from = hideCurrentNavigable(direction)
    backStackOperation.invoke(backStack)
    val to = showCurrentNavigable(direction)
    animateAndRemove(from, to, direction, navType)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    navType: NavigationType
  ) {
    val transition = DefaultTransition()
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      transition.animate(from, to, navType, direction) {
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
    maybeAttachNavigator()
    attachToLifecycle(
      currentNavigable!!, detachedState = when (direction) {
      FORWARD -> Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    when (currentState) {
      is Shown, is Resumed -> {
        containerView!!.addView(currentNavigable!!.view!!)
      }
      is Destroyed, is Created -> {
      }
    }
    return currentNavigable!!.view
  }

  protected open fun maybeAttachNavigator() {}

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
      navigateBack(GO)
      true
    } else {
      false
    }
  }

  private fun atRoot() = backStack.size <= 1
}
