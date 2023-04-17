package com.wealthfront.magellan.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.wealthfront.magellan.Direction
import kotlin.math.hypot

@Suppress("FunctionName")
public fun CircularRevealTransition(clickedView: View): MagellanTransition {
  return CircularRevealTransition(clickedView.id)
}

/**
 * A [MagellanTransition] that reveals the next screen circularly outward from the middle of the
 * [clickedViewId].
 *
 * @property clickedViewId the id of the view on which this circular reveal is centered
 */
private class CircularRevealTransition(@IdRes private val clickedViewId: Int) : MagellanTransition {

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
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      onAnimationEndCallback()
      return
    }

    val clickedViewContainer = if (direction == Direction.FORWARD) from else to
    val viewToReveal = if (direction == Direction.FORWARD) to else from!!
    val clickedViewCenter = getCenterClickedView(clickedViewContainer as ViewGroup)
    val circularRevealCenterX = clickedViewCenter[0]
    val circularRevealCenterY = clickedViewCenter[1]
    val revealedRadius = hypot(viewToReveal.width.toDouble(), viewToReveal.height.toDouble()).toFloat()
    val finalRadius = if (direction == Direction.FORWARD) revealedRadius else 0f
    val startRadius = if (direction == Direction.FORWARD) 0f else revealedRadius
    animator = ViewAnimationUtils.createCircularReveal(
      viewToReveal, circularRevealCenterX,
      circularRevealCenterY, startRadius, finalRadius
    ).apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          animator = null
          onAnimationEndCallback()
        }
      })
      interpolator = FastOutSlowInInterpolator()
    }.also { it.start() }
  }

  private fun getCenterClickedView(clickedViewContainer: ViewGroup): IntArray {
    val clickedView = clickedViewContainer.findViewById<View>(clickedViewId)
    val clickedViewRect = Rect()
    clickedView.getDrawingRect(clickedViewRect)
    clickedViewContainer.offsetDescendantRectToMyCoords(clickedView, clickedViewRect)
    return intArrayOf(
      clickedViewRect.exactCenterX().toInt(),
      clickedViewRect.exactCenterY().toInt()
    )
  }
}
