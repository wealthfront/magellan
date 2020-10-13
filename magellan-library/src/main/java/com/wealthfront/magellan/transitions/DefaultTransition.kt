package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import com.wealthfront.magellan.Direction

class DefaultTransition : MagellanTransition {

  override fun animate(from: View?, to: View, direction: Direction, callback: MagellanTransition.Callback) {
    val animator = createAnimator(from, to, direction)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        callback.onAnimationEnd()
      }
    })
    animator.start()
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
      set.play(ObjectAnimator.ofFloat(from, axis, 0f, fromTranslation.toFloat()))
    }
    set.play(ObjectAnimator.ofFloat(to, axis, toTranslation.toFloat(), 0f))
    return set
  }
}