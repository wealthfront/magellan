package com.wealthfront.magellan.core

import android.view.Menu
import android.view.View
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface Navigable : LifecycleAware {

  val view: View?

  fun onUpdateMenu(menu: Menu) {}

  fun transitionStarted() {}

  fun transitionFinished() {}
}
