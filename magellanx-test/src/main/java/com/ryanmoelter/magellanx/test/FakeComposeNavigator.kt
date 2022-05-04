package com.ryanmoelter.magellanx.test

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigationEvent
import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigator
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.navigation.LinearNavigator

/**
 * An implementation of [LinearNavigator] suitable for tests. Avoids attaching child [Navigable]s to
 * the lifecycle, avoiding the need to satisfy their dependencies. Holds state; should be
 * re-instantiated, [destroy]ed, or [clear]ed between tests.
 */
@OptIn(ExperimentalAnimationApi::class)
public class FakeComposeNavigator : ComposeNavigator() {

  public override var backStack: List<ComposeNavigationEvent> = emptyList()

  /**
   * The [Navigable] that's currently on the top of the [backStack]
   */
  public val currentNavigable: Navigable<@Composable () -> Unit>?
    get() = backStack.firstOrNull()?.navigable

  /**
   * Clear this navigator for the next test. [destroy] will do the same thing, and it's also safe to
   * leave this object to be garbage collected instead.
   */
  public fun clear() {
    backStack = emptyList()
  }

  public override fun navigate(
    direction: Direction,
    backStackOperation: (List<ComposeNavigationEvent>) -> List<ComposeNavigationEvent>
  ) {
    backStack = backStackOperation(backStack)
  }

  public override fun onDestroy(context: Context) {
    clear()
  }
}
