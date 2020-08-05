package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.Navigable
import java.util.Stack

interface Navigator {

  val backStack: List<NavigationEvent>

  fun goTo(nextNavigable: Navigable)

  fun show(nextNavigable: Navigable)

  fun replaceAndGo(nextNavigable: Navigable)

  fun replaceAndShow(nextNavigable: Navigable)

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> Unit
  )

  fun goBack(): Boolean
}
