package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.navigation.NavigationEvent

/**
 * The default transition for all [NavigationEvent]s where another [MagellanTransition] isn't
 * defined. Performs a right-to-left slide on entrance and a left-to-right slide on exit. Uses a
 * [FastOutSlowInInterpolator] for both per
 * [the Material Design guidelines](https://material.io/design/motion/speed.html#easing).
 */
public class DefaultTransition : MagellanTransition {

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
    }
    animator!!.start()
  }

  private fun createAnimator(
    from: View?,
    to: View,
    direction: Direction
  ): AnimatorSet {
    val sign = direction.sign()
    val axis: Property<View, Float> = View.TRANSLATION_X
    val toTranslation = sign * to.width
    val set = AnimatorSet()
    if (from != null) {
      val fromTranslation = sign * -from.width
      val fromAnimation = ObjectAnimator.ofFloat(from, axis, 0f, fromTranslation.toFloat())
      set.play(fromAnimation)
    }

    val toAnimation = ObjectAnimator.ofFloat(to, axis, toTranslation.toFloat(), 0f)
    set.play(toAnimation)
    set.interpolator = FastOutSlowInInterpolator()
    return set
  }
}
