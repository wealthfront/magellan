package com.wealthfront.magellan.lifecycle

import android.content.Context

interface LifecycleAware {

  fun create(context: Context) {}

  fun show(context: Context) {}

  fun resume(context: Context) {}

  fun pause(context: Context) {}

  fun hide(context: Context) {}

  fun destroy(context: Context) {}

  fun backPressed(): Boolean = false
}
