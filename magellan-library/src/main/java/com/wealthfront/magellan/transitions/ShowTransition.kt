package com.wealthfront.magellan.transitions

import android.view.View
import android.view.ViewGroup
import com.wealthfront.blend.Blend
import com.wealthfront.blend.dsl.AnimatorBuilder
import com.wealthfront.blend.dsl.fadeIn
import com.wealthfront.blend.dsl.fadeOut
import com.wealthfront.blend.dsl.scale
import com.wealthfront.blend.dsl.translationY
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD

private const val ENTER_TRANSITION_LENGTH_MILLIS = 300L
private const val EXIT_TRANSITION_LENGTH_MILLIS = 250L
private const val SCALE_DOWN_FACTOR = 0.85f
private const val HEIGHT_OFFSET_FACTOR = 0.2f

/**
 * A vertical version of [DefaultTransition]. Performs an upward slide and fade in on entrance and a
 * downward slide and fade out on exit. Uses [AnimatorBuilder.emphasizeEase] for both per
 * [the Material Design guidelines](https://material.io/design/motion/speed.html#easing).
 * The exit animation is also slightly shorter than the entrance, per the guidelines.
 */
public class ShowTransition(private val blend: Blend = Blend()) : MagellanTransition {

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    onAnimationEndCallback: () -> Unit
  ) {
    when (direction) {
      FORWARD -> animateForward(from, to, onAnimationEndCallback)
      BACKWARD -> animateBackward(from, to, onAnimationEndCallback)
    }.let { }
  }

  private fun animateForward(from: View?, to: View, onAnimationEndCallback: () -> Unit) {
    blend {
      immediate()
      target(to).animations {
        fadeOut()
        translationY(to.height * HEIGHT_OFFSET_FACTOR)
      }
    }.then {
      emphasizeEase()
      duration(ENTER_TRANSITION_LENGTH_MILLIS)
      from?.let { fromView ->
        target(fromView).animations {
          fadeOut()
          scale(SCALE_DOWN_FACTOR)
        }
      }
      target(to).animations {
        fadeIn()
        translationY(0f)
      }
      doOnFinishedEvenIfInterrupted(onAnimationEndCallback)
    }.start()
  }

  private fun animateBackward(from: View?, to: View, onAnimationEndCallback: () -> Unit) {
    blend {
      immediate()
      target(to).animations {
        fadeOut()
        scale(SCALE_DOWN_FACTOR)
      }
      doOnStart {
        // Put `to` behind `from`
        val parent = to.parent as ViewGroup
        parent.removeView(to)
        parent.addView(to, 0)
      }
    }.then {
      emphasizeEase()
      duration(EXIT_TRANSITION_LENGTH_MILLIS)
      from?.let { fromView ->
        target(fromView).animations {
          fadeOut()
          translationY(fromView.height * HEIGHT_OFFSET_FACTOR)
        }
      }
      target(to).animations {
        fadeIn()
        scale(1f)
      }
      doOnFinishedEvenIfInterrupted(onAnimationEndCallback)
    }.start()
  }
}
