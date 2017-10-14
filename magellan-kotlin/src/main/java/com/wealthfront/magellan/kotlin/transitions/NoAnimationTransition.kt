package com.wealthfront.magellan.kotlin.transitions

import android.view.View
import com.wealthfront.magellan.kotlin.Direction
import com.wealthfront.magellan.kotlin.NavigationType

class NoAnimationTransition : Transition {

  override fun animate(from: View, to: View, navType: NavigationType, direction: Direction, callback: Transition.Callback) {
    callback.onAnimationEnd()
  }

}