package com.ryanmoelter.magellanx.core.lifecycle

public interface LifecycleAware {
  public fun create() {}
  public fun show() {}
  public fun resume() {}
  public fun pause() {}
  public fun hide() {}
  public fun destroy() {}
  public fun backPressed(): Boolean = false
}
