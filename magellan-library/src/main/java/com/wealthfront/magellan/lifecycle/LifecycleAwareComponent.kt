package com.wealthfront.magellan.lifecycle

import android.content.Context

public abstract class LifecycleAwareComponent : LifecycleAware, LifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry()
  protected val lifecycleLimiter: LifecycleLimiter = LifecycleLimiter(lifecycleRegistry)

  override val children: List<LifecycleAware>
    get() = lifecycleRegistry.listeners.toList()

  override val currentState: LifecycleState
    get() = lifecycleRegistry.currentState

  final override fun create(context: Context) {
    if (currentState !is LifecycleState.Destroyed) {
      throw IllegalStateException(
        "Cannot create() from a state that is not Destroyed: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    lifecycleRegistry.create(context)
    onCreate(context)
  }

  final override fun show(context: Context) {
    if (currentState !is LifecycleState.Created) {
      throw IllegalStateException(
        "Cannot show() from a state that is not Created: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    lifecycleRegistry.show(context)
    onShow(context)
  }

  final override fun resume(context: Context) {
    if (currentState !is LifecycleState.Shown) {
      throw IllegalStateException(
        "Cannot resume() from a state that is not Shown: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    lifecycleRegistry.resume(context)
    onResume(context)
  }

  final override fun pause(context: Context) {
    if (currentState !is LifecycleState.Resumed) {
      throw IllegalStateException(
        "Cannot pause() from a state that is not Resumed: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    onPause(context)
    lifecycleRegistry.pause(context)
  }

  final override fun hide(context: Context) {
    if (currentState !is LifecycleState.Shown) {
      throw IllegalStateException(
        "Cannot hide() from a state that is not Shown: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    onHide(context)
    lifecycleRegistry.hide(context)
  }

  final override fun destroy(context: Context) {
    if (currentState !is LifecycleState.Created) {
      throw IllegalStateException(
        "Cannot destroy() from a state that is not Created: " +
          "${this::class.java.simpleName} is ${currentState::class.java.simpleName}"
      )
    }
    onDestroy(context)
    lifecycleRegistry.destroy(context)
  }

  override fun backPressed(): Boolean {
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
