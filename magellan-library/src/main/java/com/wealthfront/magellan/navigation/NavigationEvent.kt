package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Mockable
import com.wealthfront.magellan.NavigationType
import java.util.Stack

@Mockable
data class NavigationEvent(
  val navigable: NavigableCompat,
  val navigationType: NavigationType
)

internal fun Stack<NavigationEvent>.navigables(): List<NavigableCompat> {
  return map { it.navigable }
}
