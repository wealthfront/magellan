package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

public data class NavigationEvent(
  val navigable: NavigableCompat,
  val magellanTransition: MagellanTransition
)

internal fun Deque<NavigationEvent>.navigables(): List<NavigableCompat> {
  return map { it.navigable }
}
