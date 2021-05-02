package com.wealthfront.magellan.navigation

import android.view.View
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

public data class NavigationEvent(
  val navigable: NavigableCompat<View>,
  val magellanTransition: MagellanTransition
)

internal fun Deque<NavigationEvent>.navigables(): List<NavigableCompat<View>> {
  return map { it.navigable }
}
