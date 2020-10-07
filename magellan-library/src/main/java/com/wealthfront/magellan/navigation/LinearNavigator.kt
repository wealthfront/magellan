package com.wealthfront.magellan.navigation

import android.view.Menu
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.transitions.Transition
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
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun goBack(): Boolean {
    return delegate.goBack()
  }
}
