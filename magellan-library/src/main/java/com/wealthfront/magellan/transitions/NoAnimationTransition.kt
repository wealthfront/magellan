package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.magellan.Direction

class NoAnimationTransition : Transition {

  override fun animate(from: View?, to: View, direction: Direction, callback: Transition.Callback) {
    callback.onAnimationEnd()
  }
}
