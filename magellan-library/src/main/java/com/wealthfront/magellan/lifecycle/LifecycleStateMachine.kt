package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.BACKWARDS
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.FORWARD
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.NO_MOVEMENT

class LifecycleStateMachine {

  fun transitionBetweenLifecycleStates(
    lifecycleAware: LifecycleAware,
    oldState: LifecycleState,
    newState: LifecycleState
  ) {
    transitionBetweenLifecycleStates(
      listOf(lifecycleAware),
      oldState,
      newState
    )
  }

  fun transitionBetweenLifecycleStates(
    subjects: Iterable<LifecycleAware>,
    oldState: LifecycleState,
    newState: LifecycleState
  ) {
    var currentState = oldState
    while (currentState != newState) {
      currentState = when (currentState.getTheDirectionIShouldGoToGetTo(newState)) {
        FORWARD -> next(subjects, currentState, newState.context!!)
        BACKWARDS -> previous(subjects, currentState, getContext(newState, oldState))
        NO_MOVEMENT -> throw IllegalStateException("Attempting to transition from $currentState to itself")
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
        subjects.forEach { it.create(context) }
        Created(context)
      }
      is Created -> {
        subjects.forEach { it.show(context) }
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
        subjects.forEach { it.destroy(context) }
        Destroyed
      }
      is Shown -> {
        subjects.forEach { it.hide(context) }
        Created(context)
      }
      is Resumed -> {
        subjects.forEach { it.pause(context) }
        Shown(context)
      }
    }
  }

  private fun getContext(newState: LifecycleState, oldState: LifecycleState): Context {
    return newState.context ?: oldState.context!!
  }
}