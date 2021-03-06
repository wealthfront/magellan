package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.View.GONE
import com.wealthfront.magellan.Direction

public class CrossfadeTransition : MagellanTransition {

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
    from.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        from.visibility = GONE
        to.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            onAnimationEndCallback()
          }
        }).start()
      }
    }).start()
  }
}
