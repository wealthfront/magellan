package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.BACKWARDS
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.FORWARD
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.NO_MOVEMENT

internal class LifecycleStateMachine {

  fun transition(
    lifecycleAware: LifecycleAware,
    oldState: LifecycleState,
    newState: LifecycleState
  ) {
    transition(
      listOf(lifecycleAware),
      oldState,
      newState
    )
  }

  fun transition(
    subjects: Iterable<LifecycleAware>,
    oldState: LifecycleState,
    newState: LifecycleState
  ) {
    var currentState = oldState
    while (currentState.order != newState.order) {
      currentState = when (currentState.getDirectionForMovement(newState)) {
        FORWARD -> next(subjects, currentState, newState.context!!)
        BACKWARDS -> previous(subjects, currentState, oldState.context!!)
        NO_MOVEMENT -> throw IllegalStateException("Attempting to transition from $currentState to $newState")
      }
    }
  }

  private fun next(
    subjects: Iterable<LifecycleAware>,
    currentState: LifecycleState,
    context: Context
  ): LifecycleState {
    return when (currentState) {
      is Destroyed -> {
        subjects.forEach { it.create(context.applicationContext) }
        Created(context.applicationContext)
      }
      is Created -> {
        subjects.forEach { it.start(context) }
        Shown(context)
      }
      is Shown -> {
        subjects.forEach { it.resume(context) }
        Resumed(context)
      }
      is Resumed -> {
        throw IllegalStateException("Cannot go forward from resumed")
      }
    }
  }

  private fun previous(
    subjects: Iterable<LifecycleAware>,
    currentState: LifecycleState,
    context: Context
  ): LifecycleState {
    return when (currentState) {
      is Destroyed -> {
        throw IllegalStateException("Cannot go backward from destroyed")
      }
      is Created -> {
        subjects.forEach { it.destroy(context.applicationContext) }
        Destroyed
      }
      is Shown -> {
        subjects.forEach { it.stop(context) }
        Created(context.applicationContext)
      }
      is Resumed -> {
        subjects.forEach { it.pause(context) }
        Shown(context)
      }
    }
  }
}
