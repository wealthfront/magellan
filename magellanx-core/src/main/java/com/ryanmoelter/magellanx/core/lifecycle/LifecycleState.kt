package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.BACKWARDS
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.FORWARD
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.NO_MOVEMENT

public enum class LifecycleState(internal val order: Int) {

  Destroyed(0), Created(1), Shown(2), Resumed(3);

  internal fun getDirectionForMovement(other: LifecycleState) = when {
    order > other.order -> BACKWARDS
    order == other.order -> NO_MOVEMENT
    order < other.order -> FORWARD
    else -> throw IllegalStateException(
      "Unhandled order comparison: this is $order and other is ${other.order}"
    )
  }
}

public enum class LifecycleStateDirection {
  FORWARD, BACKWARDS, NO_MOVEMENT;
}
