package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable

class NavigationTraverser(private val root: Journey<*>) {

  fun getGlobalBackStack(): List<Navigable> {
    val backStackList = mutableListOf<Navigable>()
    exploreBackStack(root, backStackList)
    return backStackList.toList()
  }

  fun getVisibleNavigables(): List<Navigable> {
    val globalBackStack = getGlobalBackStack()
    return globalBackStack.filter { it.view != null }
  }

  fun logGlobalBackStack() {
    val navHistory = getGlobalBackStack().joinToString(" -> ") {
      val type = if (it is Journey<*>) { "(Journey) " } else { "(Step) " }
      type + it::class.java.simpleName
    }
    Log.i(this::class.java.simpleName, navHistory)
  }

  private fun exploreBackStack(journey: Journey<*>, backStackList: MutableList<Navigable>) {
    val backStack = getBackStackForJourney(journey)
    if (backStack.isEmpty()) return
    backStack.forEach { navEvent ->
      val nav = navEvent.navigable
      backStackList.add(nav)
      if (nav is Journey<*>) {
        exploreBackStack(nav, backStackList)
      }
    }
  }

  private fun getBackStackForJourney(journey: Journey<*>): List<NavigationEvent> {
    return journey.children.mapNotNull { it as? Navigator }.first().backStack
  }
}
