package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD

/**
 * A vertical version of [DefaultTransition]. Performs a bottom-to-top slide on entrance and a
 * top-to-bottom slide on exit. Uses a [FastOutSlowInInterpolator] for both per
 * [the Material Design guidelines](https://material.io/design/motion/speed.html#easing).
 */
public class ShowTransition : MagellanTransition {

  private var animator: Animator? = null

  override fun interrupt() {
    animator?.end()
  }

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    onAnimationEndCallback: () -> Unit
  ) {
    animator = createAnimator(from, to, direction).apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          animator = null
          onAnimationEndCallback()
        }
      })
      start()
    }
  }

  private fun createAnimator(
    from: View?,
    to: View,
    direction: Direction
  ): AnimatorSet {
    val axis: Property<View, Float> = View.TRANSLATION_Y
    val fromTranslation: Int = if (direction == FORWARD) 0 else from!!.height
    val toTranslation: Int = if (direction == BACKWARD) 0 else to.height
    val set = AnimatorSet()
    if (from != null) {
      set.play(ObjectAnimator.ofFloat(from, axis, 0f, fromTranslation.toFloat()))
    }
    set.play(ObjectAnimator.ofFloat(to, axis, toTranslation.toFloat(), 0f))
    set.interpolator = FastOutSlowInInterpolator()
    return set
  }
}
