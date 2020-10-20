package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD

class ShowTransition : MagellanTransition {

  override fun animate(from: View?, to: View, direction: Direction, onAnimationEndCallback: () -> Unit) {
    val animator = createAnimator(from, to, direction)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        onAnimationEndCallback()
      }
    })
    animator.start()
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
    return set
  }
}