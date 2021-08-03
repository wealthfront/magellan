package com.wealthfront.magellan.lifecycle

import kotlinx.coroutines.flow.StateFlow

public interface LifecycleOwner {

  public val currentStateFlow: StateFlow<LifecycleState>

  public val currentState: LifecycleState
    get() = currentStateFlow.value

  public val children: List<LifecycleAware>

  public fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)

  public fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)
}
