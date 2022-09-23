package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import java.util.Deque

public fun LinearNavigator.goBackTo(navigable: Navigable) {
  navigate(Direction.BACKWARD) { backStack ->
    checkContains(backStack, navigable)
    while (backStack.size > 1) {
      if (backStack.peek()!!.navigable == navigable) {
        break
      }
      backStack.pop()
    }
    backStack.peek()!!.magellanTransition
  }
}

public fun LinearNavigator.goBackBefore(navigable: Navigable) {
  navigate(Direction.BACKWARD) { backStack ->
    checkContains(backStack, navigable)
    while (backStack.size > 0) {
      if (backStack.pop()!!.navigable == navigable) {
        break
      }
    }
    val peek = backStack.peek()
      ?: throw IllegalStateException("No Navigable before ${navigable::class.java.simpleName}")
    peek.magellanTransition
  }
}

public fun LinearNavigator.goBackToRoot() {
  goToRoot(Direction.BACKWARD)
}

public fun LinearNavigator.goForwardToRoot() {
  goToRoot(Direction.FORWARD)
}

public fun LinearNavigator.resetWithRoot(root: Navigable) {
  navigate(Direction.FORWARD) { backStack ->
    backStack.clear()
    val transition = NoAnimationTransition()
    backStack.push(NavigationEvent(root, transition))
    transition
  }
}

public fun LinearNavigator.clearUntilRootAndGoTo(
  navigable: Navigable,
  overrideTransition: MagellanTransition? = null
) {
  navigate(Direction.FORWARD) { backStack ->
    if (backStack.isEmpty()) {
      throw IllegalStateException("Root not found in backStack")
    }
    while (backStack.size > 1) {
      backStack.pop()
    }
    val transition = overrideTransition ?: getDefaultTransition()
    backStack.push(NavigationEvent(navigable, transition))
    transition
  }
}

private fun LinearNavigator.goToRoot(direction: Direction) {
  navigate(direction) { backStack ->
    if (backStack.isEmpty()) {
      throw IllegalStateException("Root not found in backStack")
    }
    while (backStack.size > 1) {
      backStack.pop()
    }
    backStack.peek()!!.magellanTransition
  }
}

private fun checkContains(backStack: Deque<NavigationEvent>, navigable: Navigable) {
  if (!backStack.any { it.navigable == navigable }) {
    throw IllegalStateException("Navigable ${navigable::class.java.simpleName} not found in backStack")
  }
}
