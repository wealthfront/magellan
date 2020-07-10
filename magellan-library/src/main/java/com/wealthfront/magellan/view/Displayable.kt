package com.wealthfront.magellan.view

import android.view.View

interface Displayable {

  val view: View?

  fun transitionStarted() {}

  fun transitionFinished() {}
}