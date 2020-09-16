package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.blend.Blend
import com.wealthfront.magellan.Direction

class ShowTransition : Transition {

  private val blend = Blend()

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    callback: () -> Unit
  ) {
    val fromTranslation = if (direction == Direction.FORWARD) 0 else from!!.height
    val toTranslation = if (direction == Direction.BACKWARD) 0 else to.height
    blend {
      target(from!!).animations {
        translationY(fromTranslation.toFloat())
      }
      target(to).animations {
        translationY(toTranslation.toFloat())
      }
      doOnFinishedEvenIfInterrupted {
        callback()
      }
    }.start()
  }
}
