package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.core.Navigable
import java.util.Deque

data class NavigationEvent(
  val navigable: Navigable,
  val navigationType: NavigationType
)

internal fun Deque<NavigationEvent>.navigables(): List<Navigable> {
  return map { it.navigable }
}