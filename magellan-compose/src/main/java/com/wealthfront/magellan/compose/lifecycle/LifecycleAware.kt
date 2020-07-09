package com.wealthfront.magellan.compose.lifecycle

import android.content.Context
import android.view.Menu

interface LifecycleAware {

  fun create(context: Context) {}

  fun show(context: Context) {}

  fun resume(context: Context) {}

  fun pause(context: Context) {}

  fun hide(context: Context) {}

  fun destroy(context: Context) {}

  fun updateMenu(menu: Menu) {}

  fun backPressed(): Boolean = false
}
