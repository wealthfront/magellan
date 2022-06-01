package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.BACKWARDS
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.FORWARD
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.NO_MOVEMENT

public fun <T> T.transitionToState(
  newState: LifecycleState
) where T : LifecycleAware, T : LifecycleOwner {
  transition(this.currentState, newState)
}

public fun LifecycleAware.transition(oldState: LifecycleState, newState: LifecycleState) {
  listOf(this).transition(oldState, newState)
}

public fun Iterable<LifecycleAware>.transition(oldState: LifecycleState, newState: LifecycleState) {
  var currentState = oldState
  while (currentState.order != newState.order) {
    currentState = when (currentState.getDirectionForMovement(newState)) {
      FORWARD -> next(this, currentState)
      BACKWARDS -> previous(this, currentState)
      NO_MOVEMENT -> throw IllegalStateException(
        "Attempting to transition from $currentState to $newState"
      )
    }
  }
}

private fun next(
  subjects: Iterable<LifecycleAware>,
  currentState: LifecycleState
): LifecycleState {
  return when (currentState) {
    Destroyed -> {
      subjects.forEach { it.create() }
      Created
    }
    Created -> {
      subjects.forEach { it.show() }
      Shown
    }
    Shown -> {
      subjects.forEach { it.resume() }
      Resumed
    }
    Resumed -> {
      throw IllegalStateException("Cannot go forward from resumed")
    }
  }
}

private fun previous(
  subjects: Iterable<LifecycleAware>,
  currentState: LifecycleState
): LifecycleState {
  return when (currentState) {
    Destroyed -> {
      throw IllegalStateException("Cannot go backward from destroyed")
    }
    Created -> {
      subjects.forEach { it.destroy() }
      Destroyed
    }
    Shown -> {
      subjects.forEach { it.hide() }
      Created
    }
    Resumed -> {
      subjects.forEach { it.pause() }
      Shown
    }
  }
}
