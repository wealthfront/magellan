package com.wealthfront.magellan.lifecycle

import android.content.Context

public abstract class LifecycleAwareComponent : LifecycleAware, LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry()

  override val children: List<LifecycleAware>
    get() = lifecycleRegistry.listeners.toList()

  override val currentState: LifecycleState
    get() = lifecycleRegistry.currentState

  final override fun create(context: Context) {
    lifecycleRegistry.create(context)
    onCreate(context)
  }

  final override fun show(context: Context) {
    lifecycleRegistry.show(context)
    onShow(context)
  }

  final override fun resume(context: Context) {
    lifecycleRegistry.resume(context)
    onResume(context)
  }

  final override fun pause(context: Context) {
    onPause(context)
    lifecycleRegistry.pause(context)
  }

  final override fun hide(context: Context) {
    onHide(context)
    lifecycleRegistry.hide(context)
  }

  final override fun destroy(context: Context) {
    onDestroy(context)
    lifecycleRegistry.destroy(context)
  }

  final override fun backPressed(): Boolean {
    return lifecycleRegistry.backPressed() || onBackPressed()
  }

  public fun attachToLifecycle(lifecycleAware: LifecycleAware) {
    attachToLifecycle(lifecycleAware, LifecycleState.Destroyed)
  }

  public fun removeFromLifecycle(lifecycleAware: LifecycleAware) {
    removeFromLifecycle(lifecycleAware, LifecycleState.Destroyed)
  }

  override fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState) {
    lifecycleRegistry.attachToLifecycle(lifecycleAware, detachedState)
  }

  override fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState) {
    lifecycleRegistry.removeFromLifecycle(lifecycleAware, detachedState)
  }

  protected open fun onCreate(context: Context) {}

  protected open fun onShow(context: Context) {}

  protected open fun onResume(context: Context) {}

  protected open fun onPause(context: Context) {}

  protected open fun onHide(context: Context) {}

  protected open fun onDestroy(context: Context) {}

  protected open fun onBackPressed(): Boolean = false
}
