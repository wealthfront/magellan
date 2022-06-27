package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleLimit.CREATED
import com.wealthfront.magellan.lifecycle.LifecycleLimit.DESTROYED
import com.wealthfront.magellan.lifecycle.LifecycleLimit.NO_LIMIT
import com.wealthfront.magellan.lifecycle.LifecycleLimit.SHOWN

public class LifecycleRegistry : LifecycleAware, LifecycleOwner {

  internal val listeners: Set<LifecycleAware>
    get() = listenersToMaxStates.keys
  private var listenersToMaxStates: Map<LifecycleAware, LifecycleLimit> = linkedMapOf()

  override val children: List<LifecycleAware>
    get() = listeners.toList()

  override var currentState: LifecycleState = LifecycleState.Destroyed
    private set(newState) {
      val oldState = field
      field = newState
      listenersToMaxStates.forEach { (lifecycleAware, maxState) ->
        if (oldState.limitBy(maxState).order != newState.limitBy(maxState).order) {
          lifecycleAware.transition(
            oldState.limitBy(maxState),
            newState.limitBy(maxState)
          )
        }
      }
    }

  override fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState) {
    attachToLifecycleWithMaxState(lifecycleAware, NO_LIMIT, detachedState)
  }

  public fun attachToLifecycleWithMaxState(
    lifecycleAware: LifecycleAware,
    maxState: LifecycleLimit = NO_LIMIT,
    detachedState: LifecycleState = LifecycleState.Destroyed
  ) {
    if (listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot attach a lifecycleAware that is already a child: " +
          lifecycleAware::class.java.simpleName
      )
    }
    lifecycleAware.transition(detachedState, currentState.limitBy(maxState))
    listenersToMaxStates = listenersToMaxStates + (lifecycleAware to maxState)
  }

  override fun removeFromLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState
  ) {
    if (!listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot remove a lifecycleAware that is not a child: " +
          lifecycleAware::class.java.simpleName
      )
    }
    val maxState = listenersToMaxStates[lifecycleAware]!!
    listenersToMaxStates = listenersToMaxStates - lifecycleAware
    lifecycleAware.transition(currentState.limitBy(maxState), detachedState)
  }

  public fun updateMaxState(lifecycleAware: LifecycleAware, maxState: LifecycleLimit) {
    if (!listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalArgumentException(
        "Cannot update the state of a lifecycleAware that is not a child: " +
          lifecycleAware::class.java.simpleName
      )
    }
    val oldMaxState = listenersToMaxStates[lifecycleAware]!!
    if (oldMaxState != maxState) {
      val needsToTransition = !currentState.isWithinLimit(minOf(maxState, oldMaxState))
      if (needsToTransition) {
        lifecycleAware.transition(
          currentState.limitBy(oldMaxState),
          currentState.limitBy(maxState)
        )
      }
      listenersToMaxStates = listenersToMaxStates + (lifecycleAware to maxState)
    }
  }

  override fun create(context: Context) {
    currentState = LifecycleState.Created(context.applicationContext)
  }

  override fun show(context: Context) {
    currentState = LifecycleState.Shown(context)
  }

  override fun resume(context: Context) {
    currentState = LifecycleState.Resumed(context)
  }

  override fun pause(context: Context) {
    currentState = LifecycleState.Shown(context)
  }

  override fun hide(context: Context) {
    currentState = LifecycleState.Created(context.applicationContext)
  }

  override fun destroy(context: Context) {
    currentState = LifecycleState.Destroyed
  }

  override fun backPressed(): Boolean = onAllActiveListenersUntilTrue { it.backPressed() }

  private fun onAllActiveListenersUntilTrue(action: (LifecycleAware) -> Boolean): Boolean =
    listenersToMaxStates
      .asSequence()
      .filter { it.value >= NO_LIMIT }
      .map { it.key }
      .map(action)
      .any { it }
}

public enum class LifecycleLimit(internal val order: Int) {
  DESTROYED(0), CREATED(1), SHOWN(2), NO_LIMIT(3)
}

private fun LifecycleState.isWithinLimit(limit: LifecycleLimit): Boolean = order <= limit.order

private fun LifecycleState.limitBy(limit: LifecycleLimit): LifecycleState = if (isWithinLimit(limit)) {
  this
} else {
  limit.getMaxLifecycleState(context!!)
}

private fun LifecycleLimit.getMaxLifecycleState(context: Context): LifecycleState = when (this) {
  DESTROYED -> LifecycleState.Destroyed
  CREATED -> LifecycleState.Created(context)
  SHOWN -> LifecycleState.Shown(context)
  NO_LIMIT -> LifecycleState.Resumed(context)
}
