package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.magellan.Direction

public class NoAnimationTransition : MagellanTransition {

  override fun interrupt() {}

  override fun animate(from: View?, to: View, direction: Direction, onAnimationEndCallback: () -> Unit) {
    onAnimationEndCallback()
  }
}
