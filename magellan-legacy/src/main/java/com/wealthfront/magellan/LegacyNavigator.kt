package com.wealthfront.magellan

import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import com.wealthfront.magellan.transitions.Transition
import java.util.Stack

class LegacyNavigator internal constructor(
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))

  override val backStack: Stack<NavigationEvent>
    get() = delegate.backStack

  init {
    delegate.currentNavigableSetup = { navItem ->
      if (navItem is Screen<*>) {
        navItem.setNavigator(this)
      }
    }
  }

  @JvmOverloads
  fun goTo(navigable: NavigableCompat, overrideTransition: Transition? = null) {
    delegate.goTo(navigable, overrideTransition)
  }

  @JvmOverloads
  fun show(navigable: NavigableCompat, overrideTransition: Transition? = null) {
    delegate.show(navigable, overrideTransition)
  }

  @JvmOverloads
  fun replaceAndGo(navigable: NavigableCompat, overrideTransition: Transition? = null) {
    delegate.replaceAndGo(navigable, overrideTransition)
  }

  @JvmOverloads
  fun replaceAndShow(navigable: NavigableCompat, overrideTransition: Transition? = null) {
    delegate.replaceAndShow(navigable, overrideTransition)
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> Unit
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun goBack(): Boolean {
    return delegate.goBack()
  }
}
