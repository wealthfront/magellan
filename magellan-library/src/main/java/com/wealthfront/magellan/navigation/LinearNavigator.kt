package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.transitions.Transition
import java.util.Stack

class LinearNavigator internal constructor(
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))

  override val backStack: Stack<NavigationEvent>
    get() = delegate.backStack

  fun goTo(navigable: Navigable, overrideTransition: Transition? = null) {
    delegate.goTo(navigable, overrideTransition)
  }

  fun show(navigable: Navigable, overrideTransition: Transition? = null) {
    delegate.show(navigable, overrideTransition)
  }

  fun replaceAndGo(navigable: Navigable, overrideTransition: Transition? = null) {
    delegate.replaceAndGo(navigable, overrideTransition)
  }

  fun replaceAndShow(navigable: Navigable, overrideTransition: Transition? = null) {
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
