package com.wealthfront.magellan.test

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.navigation.LinearNavigator
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.ArrayDeque
import java.util.Deque

/**
 * An implementation of [LinearNavigator] suitable for tests. Avoids attaching child [Navigable]s to
 * the lifecycle, avoiding the need to satisfy their dependencies. Holds state; should be
 * re-instantiated, [destroy]ed, or [clear]ed between tests.
 */
public class FakeLinearNavigator : LinearNavigator {

  public override var backStack: List<NavigationEvent> = emptyList()

  /**
   * The [Navigable] that's currently on the top of the [backStack]
   */
  public val currentNavigable: Navigable?
    get() = backStack.firstOrNull()?.navigable as? Navigable

  /**
   * Clear this navigator for the next test. [destroy] will do the same thing, and it's also safe to
   * leave this object to be garbage collected instead.
   */
  public fun clear() {
    backStack = emptyList()
  }

  public override fun goTo(navigable: Navigable, overrideMagellanTransition: MagellanTransition?) {
    navigate(FORWARD) { backStack ->
      backStack.push(
        NavigationEvent(
          navigable,
          overrideMagellanTransition ?: getDefaultTransition()
        )
      )
      backStack.peek()!!
    }
  }

  public override fun replace(navigable: Navigable, overrideMagellanTransition: MagellanTransition?) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(
        NavigationEvent(
          navigable,
          overrideMagellanTransition ?: getDefaultTransition()
        )
      )
      backStack.peek()!!
    }
  }

  public override fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> NavigationEvent
  ) {
    val tempBackStack: Deque<NavigationEvent> = ArrayDeque(backStack)
    backStackOperation(tempBackStack)
    backStack = tempBackStack.toList()
  }

  public override fun goBack(): Boolean {
    return if (backStack.size > 1) {
      navigate(BACKWARD) { backStack -> backStack.pop() }
      true
    } else {
      false
    }
  }

  public override fun destroy(context: Context) {
    clear()
  }
}
