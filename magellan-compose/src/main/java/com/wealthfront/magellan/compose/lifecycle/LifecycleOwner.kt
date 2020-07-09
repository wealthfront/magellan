package com.wealthfront.magellan.compose.lifecycle

interface LifecycleOwner {

  val currentState: LifecycleState

  fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)

  fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)
}