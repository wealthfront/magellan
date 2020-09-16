package com.wealthfront.magellan.navigation

import android.view.Menu
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import java.util.Stack

class LinearNavigator internal constructor(
  override val journey: Journey<*>,
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
