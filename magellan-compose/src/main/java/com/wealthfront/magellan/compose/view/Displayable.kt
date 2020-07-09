package com.wealthfront.magellan.compose.view

import android.view.View

interface Displayable {

  val view: View?

  fun transitionStarted() {}

  fun transitionFinished() {}
}