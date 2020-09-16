package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.blend.Blend
import com.wealthfront.magellan.Direction

class DefaultTransition : Transition {

  private val blend = Blend()

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    callback: () -> Unit
  ) {
    val sign = direction.sign()
    val fromTranslation = sign * -from!!.width
    val toTranslation = sign * to.width
    blend {
      target(from).animations {
        translationX(fromTranslation.toFloat())
      }
      target(to).animations {
        translationX(toTranslation.toFloat())
      }
      doOnFinishedEvenIfInterrupted {
        callback()
      }
    }.start()
  }
}
