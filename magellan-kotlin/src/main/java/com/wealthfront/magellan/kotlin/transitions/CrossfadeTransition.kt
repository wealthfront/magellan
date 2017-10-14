package com.wealthfront.magellan.kotlin.transitions

import com.wealthfront.magellan.kotlin.NavigationType
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.wealthfront.magellan.kotlin.Direction


class CrossfadeTransition : Transition {

  override fun animate(from: View, to: View, navType: NavigationType, direction: Direction, callback: Transition.Callback) {
    to.alpha = 0f
    from.alpha = 1f
    to.visibility = VISIBLE
    from.visibility = VISIBLE

    from.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        from.visibility = GONE
        to.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            callback.onAnimationEnd()
          }
        }).start()
      }
    }).start()
  }

}