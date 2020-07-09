package com.wealthfront.magellan.compose.lifecycle

import android.content.Context
import android.view.Menu

abstract class LifecycleAwareComponent : LifecycleAware, LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry()

  override val currentState get() = lifecycleRegistry.currentState

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

  final override fun updateMenu(menu: Menu) {
    onUpdateMenu(menu)
    lifecycleRegistry.updateMenu(menu)
  }

  final override fun backPressed(): Boolean {
    return lifecycleRegistry.backPressed() || onBackPressed()
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

  protected open fun onUpdateMenu(menu: Menu) {}

  protected open fun onBackPressed(): Boolean = false
}