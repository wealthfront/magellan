package com.example.magellan.compose.transitions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Up
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.Navigable

@OptIn(ExperimentalAnimationApi::class)
public interface MagellanComposeTransition {

  public fun getTransitionForDirection(
    direction: Direction
  ): AnimatedContentScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform
}

@OptIn(ExperimentalAnimationApi::class)
public class SimpleComposeTransition(
  public val transitionSpec: AnimatedContentScope<Navigable<@Composable () -> Unit>?>.(Direction) -> ContentTransform
) : MagellanComposeTransition {

  public override fun getTransitionForDirection(
    direction: Direction
  ): AnimatedContentScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform {
    return { transitionSpec(direction) }
  }
}

@OptIn(ExperimentalAnimationApi::class)
public val defaultTransition: SimpleComposeTransition = SimpleComposeTransition {
  when (it) {
    Direction.FORWARD -> slideIntoContainer(Left) with slideOutOfContainer(Left)
    Direction.BACKWARD -> slideIntoContainer(Right) with slideOutOfContainer(Right)
  }
}

@OptIn(ExperimentalAnimationApi::class)
public val showTransition: SimpleComposeTransition = SimpleComposeTransition {
  when (it) {
    Direction.FORWARD -> slideIntoContainer(Up) with ExitTransition.None
    Direction.BACKWARD -> EnterTransition.None with slideOutOfContainer(Down)
  }
}

@OptIn(ExperimentalAnimationApi::class)
public val noTransition: SimpleComposeTransition = SimpleComposeTransition {
  EnterTransition.None with ExitTransition.None
}

// TODO: Add more transition specs
