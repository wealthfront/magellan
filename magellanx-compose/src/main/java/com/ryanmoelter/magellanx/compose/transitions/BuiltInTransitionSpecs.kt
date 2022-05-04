package com.ryanmoelter.magellanx.compose.transitions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Up
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.core.Navigable
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
public interface MagellanComposeTransition {

  @Composable
  public fun getTransitionForDirection(
    direction: Direction
  ): AnimatedContentScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform
}

@OptIn(ExperimentalAnimationApi::class)
public class SimpleComposeTransition(
  /* ktlint-disable max-line-length */
  public val transitionSpec: AnimatedContentScope<Navigable<@Composable () -> Unit>?>.(Direction) -> ContentTransform
  /* ktlint-enable max-line-length */
) : MagellanComposeTransition {

  @Composable
  public override fun getTransitionForDirection(
    direction: Direction
  ): AnimatedContentScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform {
    return { transitionSpec(direction) }
  }
}

private const val SCALE_DOWN_FACTOR = 0.85f
private const val SCALE_UP_FACTOR = 1.15f

@OptIn(ExperimentalAnimationApi::class)
public val defaultTransition: SimpleComposeTransition = SimpleComposeTransition {
  when (it) {
    Direction.FORWARD -> {
      fadeIn() + scaleIn(initialScale = SCALE_DOWN_FACTOR) with
        fadeOut() + scaleOut(targetScale = SCALE_UP_FACTOR)
    }
    Direction.BACKWARD -> {
      fadeIn() + scaleIn(initialScale = SCALE_UP_FACTOR) with
        fadeOut() + scaleOut(targetScale = SCALE_DOWN_FACTOR)
    }
  }
}

private const val HEIGHT_OFFSET_FACTOR = 0.8f

@OptIn(ExperimentalAnimationApi::class)
public val showTransition: SimpleComposeTransition = SimpleComposeTransition { direction ->
  when (direction) {
    Direction.FORWARD -> {
      fadeIn() +
        slideIntoContainer(
          Up,
          initialOffset = { height -> (height * HEIGHT_OFFSET_FACTOR).roundToInt() }
        ) with
        fadeOut() + scaleOut(targetScale = SCALE_UP_FACTOR)
    }
    Direction.BACKWARD -> {
      fadeIn() + scaleIn(initialScale = SCALE_UP_FACTOR) with
        fadeOut() + slideOutOfContainer(
          Up,
          targetOffset = { height -> (height * HEIGHT_OFFSET_FACTOR).roundToInt() }
        )
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
public val noTransition: SimpleComposeTransition = SimpleComposeTransition {
  EnterTransition.None with ExitTransition.None
}

// TODO: Add more transition specs
