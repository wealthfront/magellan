package com.wealthfront.magellan.lifecycle

public interface LifecycleOwner {

  public val currentState: LifecycleState

  public val children: List<LifecycleAware>

  public fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)

  public fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)
}
