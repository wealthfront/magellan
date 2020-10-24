package com.wealthfront.magellan.lifecycle

import android.annotation.SuppressLint
import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.BACKWARDS
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.FORWARD
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.NO_MOVEMENT

public sealed class LifecycleState(public open val context: Context? = null, private val order: Int) {

  @SuppressLint("StaticFieldLeak")
  public object Destroyed : LifecycleState(null, 0)
  public data class Created(override val context: Context) : LifecycleState(context, 1)
  public data class Shown(override val context: Context) : LifecycleState(context, 2)
  public data class Resumed(override val context: Context) : LifecycleState(context, 3)

  internal fun getTheDirectionIShouldGoToGetTo(other: LifecycleState) = when {
    order > other.order -> BACKWARDS
    order == other.order -> NO_MOVEMENT
    order < other.order -> FORWARD
    else -> throw IllegalStateException("Unhandled order comparison: this is $order and other is ${other.order}")
  }

  internal fun getEarlierOfCurrentState(): LifecycleState = when (this) {
    is Destroyed -> Destroyed
    is Created, is Shown, is Resumed -> Created(context!!)
  }
}

public enum class LifecycleStateDirection {
  FORWARD, BACKWARDS, NO_MOVEMENT;
}
