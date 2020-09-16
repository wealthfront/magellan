package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.magellan.Direction

interface SharedElementTransition {

  fun animate(
    from: View,
    to: View,
    direction: Direction,
    callback: () -> Unit
  )
}