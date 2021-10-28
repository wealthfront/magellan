package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

public interface LinearNavigator : Navigator, LifecycleAware {

  public fun goTo(navigable: Navigable, overrideMagellanTransition: MagellanTransition? = null)

  public fun replace(navigable: Navigable, overrideMagellanTransition: MagellanTransition? = null)

  public fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> MagellanTransition
  )
}
