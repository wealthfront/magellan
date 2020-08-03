package com.wealthfront.magellan.core

import android.view.View
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface Navigable : LifecycleAware {

  val view: View?

  fun transitionStarted() {}

  fun transitionFinished() {}
}
