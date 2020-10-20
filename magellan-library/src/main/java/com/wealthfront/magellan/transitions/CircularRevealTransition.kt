package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.wealthfront.magellan.Direction
import kotlin.math.hypot

class CircularRevealTransition(private val clickedView: View) : MagellanTransition {

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    onAnimationEndCallback: () -> Unit
  ) {
    val clickedViewCenter = getCenterClickedView(from as ViewGroup)
    val circularRevealCenterX = clickedViewCenter[0]
    val circularRevealCenterY = clickedViewCenter[1]
    val finalRadius = hypot(to.width.toDouble(), to.height.toDouble()).toFloat()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val anim = ViewAnimationUtils.createCircularReveal(
        to, circularRevealCenterX,
        circularRevealCenterY, 0f, finalRadius
      )
      anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          onAnimationEndCallback()
        }
      })
      anim.start()
    } else {
      onAnimationEndCallback()
    }
  }

  private fun getCenterClickedView(from: ViewGroup): IntArray {
    val clickedViewRect = Rect()
    clickedView.getDrawingRect(clickedViewRect)
    from.offsetDescendantRectToMyCoords(clickedView, clickedViewRect)
    return intArrayOf(
      clickedViewRect.exactCenterX().toInt(),
      clickedViewRect.exactCenterY().toInt()
    )
  }
}
