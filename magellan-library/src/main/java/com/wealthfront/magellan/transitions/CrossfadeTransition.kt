package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.wealthfront.magellan.Direction

class CrossfadeTransition : MagellanTransition {

  override fun animate(from: View?, to: View, direction: Direction, callback: MagellanTransition.Callback) {
    to.alpha = 0f
    from!!.alpha = 1f
    to.visibility = View.VISIBLE
    from.visibility = View.VISIBLE
    from.animate().alpha(0f).setListener(
      object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          from.visibility = View.GONE
          to.animate().alpha(1f).setListener(
            object : AnimatorListenerAdapter() {
              override fun onAnimationEnd(animation: Animator) {
                callback.onAnimationEnd()
              }
            }
          ).start()
        }
      }
    ).start()
  }
}
