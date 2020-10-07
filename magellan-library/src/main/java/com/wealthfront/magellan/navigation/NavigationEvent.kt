package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.transitions.Transition
import java.util.Stack

data class NavigationEvent(
  val navigable: NavigableCompat,
  val transition: Transition
)

internal fun Stack<NavigationEvent>.navigables(): List<NavigableCompat> {
  return map { it.navigable }
}
