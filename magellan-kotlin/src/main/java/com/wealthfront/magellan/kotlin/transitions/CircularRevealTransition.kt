package com.wealthfront.magellan.kotlin.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.wealthfront.magellan.kotlin.Direction
import com.wealthfront.magellan.kotlin.NavigationType

class CircularRevealTransition(private val clickedView: View) : Transition {

  override fun animate(from: View, to: View, navType: NavigationType, direction: Direction, callback: Transition.Callback) {
    val clickedViewCenter = getCenterClickedView(from as ViewGroup)
    val circularRevealCenterX = clickedViewCenter[0]
    val circularRevealCenterY = clickedViewCenter[1]
    val finalRadius = Math.hypot(to.width.toDouble(), to.height.toDouble()).toFloat()

    if (SDK_INT >= LOLLIPOP) {
      val anim = ViewAnimationUtils.createCircularReveal(to, circularRevealCenterX,
          circularRevealCenterY, 0f, finalRadius)
      anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          callback.onAnimationEnd()
        }
      })
      anim.start()
    } else {
      callback.onAnimationEnd()
    }
  }

  private fun getCenterClickedView(from: ViewGroup): IntArray {
    val clickedViewRect = Rect()
    clickedView.getDrawingRect(clickedViewRect)
    from.offsetDescendantRectToMyCoords(clickedView, clickedViewRect)
    return intArrayOf(clickedViewRect.exactCenterX().toInt(), clickedViewRect.exactCenterY().toInt())
  }

}
