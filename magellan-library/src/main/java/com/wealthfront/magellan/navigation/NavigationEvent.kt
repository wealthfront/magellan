package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

public data class NavigationEvent<ViewType : Any>(
  val navigable: NavigableCompat<ViewType>,
  val magellanTransition: MagellanTransition
)

internal fun <ViewType : Any> Deque<NavigationEvent<ViewType>>.navigables(): List<NavigableCompat<ViewType>> {
  return map { it.navigable }
}
