package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.magellan.Direction

class NoAnimationTransition : MagellanTransition {

  override fun animate(from: View?, to: View, direction: Direction, onAnimationEndCallback: () -> Unit) {
    onAnimationEndCallback()
  }
}
