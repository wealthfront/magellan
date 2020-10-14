package com.wealthfront.magellan

import android.view.Menu
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition
import java.util.Stack

class LegacyNavigator internal constructor(
  override val journey: Step<*>,
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(journey, container))

  override val backStack: Stack<NavigationEvent>
    get() = delegate.backStack

  internal var menu: Menu? = null
    set(value) {
      field = value
      delegate.menu = value
    }

  init {
    delegate.currentNavigableSetup = { navItem ->
      if (navItem is Screen<*>) {
        navItem.setNavigator(this)
      }
      if (navItem is MultiScreen<*>) {
        navItem.screens.forEach { it.setNavigator(this) }
      }
    }
  }

  fun rewriteHistory(backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent) {
    backStackOperation.invoke(backStack)
  }

  fun navigate(backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent) {
    navigate(FORWARD, backStackOperation)
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun replace(navigable: NavigableCompat) {
    delegate.replace(navigable)
  }

  fun replaceNow(navigable: NavigableCompat) {
    delegate.replace(navigable, NoAnimationTransition())
  }

  fun show(navigable: NavigableCompat) {
    delegate.goTo(navigable, ShowTransition())
  }

  fun showNow(navigable: NavigableCompat) {
    delegate.goTo(navigable, NoAnimationTransition())
  }

  fun goTo(navigable: NavigableCompat) {
    delegate.goTo(navigable)
  }

  fun hide(navigable: NavigableCompat) {
    navigate(BACKWARD) { backStack ->
      backStack.pop()
    }
  }

  fun hideNow(navigable: NavigableCompat) {
    navigate(BACKWARD) { backStack ->
      NavigationEvent(backStack.pop().navigable, NoAnimationTransition())
    }
  }

  fun goBackToRoot() {
    navigate(Direction.BACKWARD) { history ->
      var navigable: NavigableCompat? = null
      while (history.size > 1) {
        navigable = history.pop().navigable
      }
      NavigationEvent(navigable!!, DefaultTransition())
    }
  }

  fun goBack(): Boolean {
    return delegate.goBack()
  }
}
