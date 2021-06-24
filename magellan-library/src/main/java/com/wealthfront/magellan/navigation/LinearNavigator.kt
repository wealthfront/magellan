package com.wealthfront.magellan.navigation

import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

@OpenForMocking
public class LinearNavigator internal constructor(
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))

  override val backStack: List<NavigationEvent>
    get() = delegate.backStack.toList()

  public fun goTo(navigable: Navigable<View>, overrideMagellanTransition: MagellanTransition? = null) {
    delegate.goTo(navigable, overrideMagellanTransition)
  }

  public fun replace(navigable: Navigable<View>, overrideMagellanTransition: MagellanTransition? = null) {
    delegate.replace(navigable, overrideMagellanTransition)
  }

  public fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  public override fun goBack(): Boolean {
    return delegate.goBack()
  }
}
