package com.wealthfront.magellan.navigation

import android.view.Menu
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Stack

public class LinearNavigator internal constructor(
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

  public fun goTo(navigable: Navigable, overrideMagellanTransition: MagellanTransition? = null) {
    delegate.goTo(navigable, overrideMagellanTransition)
  }

  public fun replace(navigable: NavigableCompat, overrideMagellanTransition: MagellanTransition? = null) {
    delegate.replace(navigable, overrideMagellanTransition)
  }

  public fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  public fun goBack(): Boolean {
    return delegate.goBack()
  }
}
