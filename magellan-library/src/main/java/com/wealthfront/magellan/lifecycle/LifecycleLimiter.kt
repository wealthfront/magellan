package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.OpenForMocking

@OpenForMocking
public class LifecycleLimiter : LifecycleOwner, LifecycleAware {

  private val lifecycleRegistry = LifecycleRegistry()

  override val children: List<LifecycleAware>
    get() = lifecycleRegistry.listenersToMaxStates.keys.toList()

  override val currentState: LifecycleState
    get() = lifecycleRegistry.currentState

  override fun create(context: Context) {
    lifecycleRegistry.create(context)
  }

  override fun start(context: Context) {
    lifecycleRegistry.start(context)
  }

  override fun resume(context: Context) {
    lifecycleRegistry.resume(context)
  }

  override fun pause(context: Context) {
    lifecycleRegistry.pause(context)
  }

  override fun stop(context: Context) {
    lifecycleRegistry.stop(context)
  }

  override fun destroy(context: Context) {
    lifecycleRegistry.destroy(context)
  }

  override fun backPressed(): Boolean = lifecycleRegistry.backPressed()

  override fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState) {
    attachToLifecycleWithMaxState(lifecycleAware, LifecycleLimit.NO_LIMIT, detachedState)
  }

  public fun attachToLifecycleWithMaxState(lifecycleAware: LifecycleAware, maxState: LifecycleLimit, detachedState: LifecycleState = LifecycleState.Destroyed) {
    lifecycleRegistry.attachToLifecycle(lifecycleAware, detachedState, maxState)
  }

  public fun updateMaxStateForChild(lifecycleAware: LifecycleAware, newMaxState: LifecycleLimit) {
    lifecycleRegistry.updateMaxState(lifecycleAware, newMaxState)
  }

  override fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState) {
    lifecycleRegistry.removeFromLifecycle(lifecycleAware, detachedState)
  }
}
