package com.wealthfront.magellan.compose.lifecycle

import android.content.Context
import com.wealthfront.magellan.compose.lifecycle.LifecycleStateDirection.BACKWARDS
import com.wealthfront.magellan.compose.lifecycle.LifecycleStateDirection.FORWARD
import com.wealthfront.magellan.compose.lifecycle.LifecycleStateDirection.NO_MOVEMENT

sealed class LifecycleState(open val context: Context? = null, private val order: Int) {

  object Destroyed : LifecycleState(null, 0)
  data class Created(override val context: Context) : LifecycleState(context, 1)
  data class Shown(override val context: Context) : LifecycleState(context, 2)
  data class Resumed(override val context: Context) : LifecycleState(context, 3)

  fun getTheDirectionIShouldGoToGetTo(other: LifecycleState) = when {
    order > other.order -> BACKWARDS
    order == other.order -> NO_MOVEMENT
    order < other.order -> FORWARD
    else -> throw IllegalStateException("Unhandled order comparison: this is $order and other is ${other.order}")
  }

  fun getEarlierOfCurrentState(): LifecycleState = when (this) {
    is Destroyed -> Destroyed
    is Created, is Shown, is Resumed -> Created(context!!)
  }
}

enum class LifecycleStateDirection {
  FORWARD, BACKWARDS, NO_MOVEMENT;
}
