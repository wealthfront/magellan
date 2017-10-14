package com.wealthfront.magellan.kotlin.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View
import android.view.View.TRANSLATION_X
import android.view.View.TRANSLATION_Y
import com.wealthfront.magellan.kotlin.Direction
import com.wealthfront.magellan.kotlin.Direction.BACKWARD
import com.wealthfront.magellan.kotlin.Direction.FORWARD
import com.wealthfront.magellan.kotlin.NavigationType

class DefaultTransition : Transition {

  override fun animate(from: View, to: View, navType: NavigationType, direction: Direction, callback: Transition.Callback) {
    val animator = createAnimator(from, to, navType, direction)
    animator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        callback.onAnimationEnd()
      }
    })
    animator.start()
  }

  private fun createAnimator(from: View, to: View, navType: NavigationType, direction: Direction): AnimatorSet {
    val axis: Property<View, Float>
    val fromTranslation: Int
    val toTranslation: Int
    val sign = direction.sign()

    when (navType) {
      NavigationType.GO -> {
        axis = TRANSLATION_X
        fromTranslation = sign * -from.width
        toTranslation = sign * to.width
      }
      NavigationType.SHOW -> {
        axis = TRANSLATION_Y
        fromTranslation = if (direction == FORWARD) 0 else from.height
        toTranslation = if (direction == BACKWARD) 0 else to.height
      }
      else -> {
        axis = TRANSLATION_X
        fromTranslation = 0
        toTranslation = 0
      }
    }
    return AnimatorSet().apply {
      play(ObjectAnimator.ofFloat(from, axis, 0f, fromTranslation.toFloat()))
      play(ObjectAnimator.ofFloat(to, axis, toTranslation.toFloat(), 0f))
    }
  }
}
