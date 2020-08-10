package com.wealthfront.magellan.lifecycle

import android.content.Context

interface LifecycleAware {

  @JvmDefault fun create(context: Context) {}

  @JvmDefault fun show(context: Context) {}

  @JvmDefault fun resume(context: Context) {}

  @JvmDefault fun pause(context: Context) {}

  @JvmDefault fun hide(context: Context) {}

  @JvmDefault fun destroy(context: Context) {}

  @JvmDefault fun backPressed() = false
}
