package com.ryanmoelter.magellanx.core.lifecycle

import android.content.Context

public interface LifecycleAware {
  public fun create(context: Context) {}
  public fun show(context: Context) {}
  public fun resume(context: Context) {}
  public fun pause(context: Context) {}
  public fun hide(context: Context) {}
  public fun destroy(context: Context) {}
  public fun backPressed(): Boolean = false
}
