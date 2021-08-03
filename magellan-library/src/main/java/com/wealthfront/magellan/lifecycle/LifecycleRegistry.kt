package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleLimit.CREATED
import com.wealthfront.magellan.lifecycle.LifecycleLimit.DESTROYED
import com.wealthfront.magellan.lifecycle.LifecycleLimit.NO_LIMIT
import com.wealthfront.magellan.lifecycle.LifecycleLimit.SHOWN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class LifecycleRegistry : LifecycleAware {

  val listeners: Set<LifecycleAware>
    get() = listenersToMaxStates.keys

  var listenersToMaxStates: Map<LifecycleAware, LifecycleLimit> = linkedMapOf()
    private set

  private val lifecycleStateMachine = LifecycleStateMachine()

  private val _currentStateFlow: MutableStateFlow<LifecycleState> = MutableStateFlow(LifecycleState.Destroyed)
  internal val currentStateFlow: StateFlow<LifecycleState> get() = _currentStateFlow.asStateFlow()

  internal var currentState: LifecycleState
    get() = currentStateFlow.value
    private set(newState) {
      val oldState = currentStateFlow.value
      listenersToMaxStates.forEach { (lifecycleAware, maxState) ->
        if (oldState.limitBy(maxState).order != newState.limitBy(maxState).order) {
          lifecycleStateMachine.transition(
            lifecycleAware,
            oldState.limitBy(maxState),
            newState.limitBy(maxState)
          )
        }
      }
      _currentStateFlow.value = newState
    }

  fun attachToLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState = LifecycleState.Destroyed,
    maxState: LifecycleLimit = NO_LIMIT
  ) {
    if (listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot attach a lifecycleAware that is already a child: " +
          lifecycleAware::class.java.simpleName
      )
    }
    lifecycleStateMachine.transition(lifecycleAware, detachedState, currentState.limitBy(maxState))
    listenersToMaxStates = listenersToMaxStates + (lifecycleAware to maxState)
  }

  fun removeFromLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState = LifecycleState.Destroyed
  ) {
    if (!listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot remove a lifecycleAware that is not a child: " +
          lifecycleAware::class.java.simpleName
      )
    }
    val maxState = listenersToMaxStates[lifecycleAware]!!
    listenersToMaxStates = listenersToMaxStates - lifecycleAware
    lifecycleStateMachine.transition(lifecycleAware, currentState.limitBy(maxState), detachedState)
  }

  fun updateMaxState(lifecycleAware: LifecycleAware, maxState: LifecycleLimit) {
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
        lifecycleStateMachine.transition(
          lifecycleAware,
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
      .filter { it.value >= SHOWN }
      .map { it.key }
      .map(action)
      .any { it }
}

public enum class LifecycleLimit(internal val order: Int) {
  DESTROYED(0), CREATED(1), SHOWN(2), NO_LIMIT(3)
}

public fun LifecycleState.isWithinLimit(limit: LifecycleLimit): Boolean = order <= limit.order

public fun LifecycleState.limitBy(limit: LifecycleLimit): LifecycleState = if (isWithinLimit(limit)) {
  this
} else {
  limit.getMaxLifecycleState(context!!)
}

public fun LifecycleLimit.getMaxLifecycleState(context: Context): LifecycleState = when (this) {
  DESTROYED -> LifecycleState.Destroyed
  CREATED -> LifecycleState.Created(context)
  SHOWN -> LifecycleState.Shown(context)
  NO_LIMIT -> LifecycleState.Resumed(context)
}
