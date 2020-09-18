package com.wealthfront.magellan

import android.view.Menu
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import java.util.Stack

open class LegacyNavigator internal constructor(
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

  fun replaceNow(navigable: NavigableCompat) {
    delegate.replaceNow(navigable)
  }

  fun hide(navigable: NavigableCompat) {
    delegate.hide(navigable)
  }

  fun currentScreen() = backStack.peek().navigable

  fun isCurrentScreen(navigable: NavigableCompat) = currentScreen() == navigable

  fun goBackToRoot(navType: NavigationType) {
    navigate(Direction.BACKWARD) { history ->
      var navigable: NavigableCompat? = null
      while (history.size > 1) {
        navigable = history.pop().navigable
      }
      NavigationEvent(navigable!!, NavigationType.GO)
    }
  }

  fun atRoot() = backStack.size == 1

  fun goTo(navigable: NavigableCompat) {
    delegate.goTo(navigable)
  }

  fun show(navigable: NavigableCompat) {
    delegate.show(navigable)
  }

  fun replaceAndGo(navigable: NavigableCompat) {
    delegate.replaceAndGo(navigable)
  }

  fun replaceAndShow(navigable: NavigableCompat) {
    delegate.replaceAndShow(navigable)
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun goBack() = delegate.goBack()
}
