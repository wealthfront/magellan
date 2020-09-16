package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.blend.Blend
import com.wealthfront.magellan.Direction

class CrossfadeTransition : Transition {

  private val blend = Blend()

  override fun animate(
    from: View?,
    to: View,
    direction: Direction,
    callback: () -> Unit
  ) {
    blend {
      target(from!!).animations {
        fadeOut()
      }
      target(to).animations {
        fadeIn()
      }
      doOnFinishedEvenIfInterrupted {
        callback()
      }
    }.start()
  }
}
