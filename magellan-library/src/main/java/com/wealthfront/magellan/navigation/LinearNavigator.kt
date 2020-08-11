package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Mockable
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import java.util.Stack

@Mockable
class LinearNavigator internal constructor(
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))

  override val backStack: Stack<NavigationEvent>
    get() = delegate.backStack

  fun goTo(navigable: Navigable) {
    delegate.goTo(navigable)
  }

  fun show(navigable: Navigable) {
    delegate.show(navigable)
  }

  fun replaceAndGo(navigable: Navigable) {
    delegate.replaceAndGo(navigable)
  }

  fun replaceAndShow(navigable: Navigable) {
    delegate.replaceAndShow(navigable)
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun goBack(): Boolean {
    return delegate.goBack()
  }
}
