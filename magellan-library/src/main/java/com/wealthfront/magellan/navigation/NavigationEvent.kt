package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Stack

data class NavigationEvent(
  val navigable: NavigableCompat,
  val magellanTransition: MagellanTransition
)

internal fun Stack<NavigationEvent>.navigables(): List<NavigableCompat> {
  return map { it.navigable }
}
