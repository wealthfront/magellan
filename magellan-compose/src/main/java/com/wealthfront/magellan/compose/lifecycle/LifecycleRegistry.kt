package com.wealthfront.magellan.compose.lifecycle

import android.content.Context
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Shown

internal class LifecycleRegistry : LifecycleAware {

  private var listeners: List<LifecycleAware> = emptyList()
  private val lifecycleStateMachine = LifecycleStateMachine()

  internal var currentState: LifecycleState = Destroyed
    private set(newState) {
      val oldState = field
      field = newState
      if (listeners.isNotEmpty()) {
        lifecycleStateMachine.transitionBetweenLifecycleStates(listeners, oldState, newState)
      }
    }

  fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = Destroyed) {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, detachedState, currentState)
    listeners = listeners + lifecycleAware
  }

  fun removeFromLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState = Destroyed
  ) {
    listeners = listeners - lifecycleAware
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, currentState, detachedState)
  }

  override fun create(context: Context) {
    currentState = Created(context)
  }

  override fun show(context: Context) {
    currentState = Shown(context)
  }

  override fun resume(context: Context) {
    currentState = Resumed(context)
  }

  override fun pause(context: Context) {
    currentState = Shown(context)
  }

  override fun hide(context: Context) {
    currentState = Created(context)
  }

  override fun destroy(context: Context) {
    currentState = Destroyed
  }

  override fun backPressed(): Boolean = onAllListenersUntilTrue { it.backPressed() }

  private fun onAllListenersUntilTrue(action: (LifecycleAware) -> Boolean): Boolean =
    listeners.asSequence().map(action).any { it }
}
