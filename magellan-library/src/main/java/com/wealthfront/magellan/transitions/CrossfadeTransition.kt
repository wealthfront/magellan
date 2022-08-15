package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import android.view.View.GONE
import com.wealthfront.magellan.Direction

public class CrossfadeTransition : MagellanTransition {

  private var animationSet: Animator? = null

  override fun interrupt() {
    animationSet?.end()
  }

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    onAnimationEndCallback: () -> Unit
  ) {
    to.alpha = 0f
    from!!.alpha = 1f
    to.visibility = View.VISIBLE
    from.visibility = View.VISIBLE

    animationSet = createAnimator(from, to).apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          animationSet = null
          onAnimationEndCallback()
        }
      })
    }
    animationSet!!.start()
  }

  private fun createAnimator(
    from: View,
    to: View
  ): AnimatorSet {
    val axis: Property<View, Float> = View.ALPHA
    val fromAnimator = ObjectAnimator.ofFloat(from, axis, from.alpha, 0f).apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          from.visibility = GONE
        }
      })
    }

    val toAnimator = ObjectAnimator.ofFloat(to, axis, to.alpha, 1f)
    return AnimatorSet().apply {
      playSequentially(fromAnimator, toAnimator)
    }
  }
}
