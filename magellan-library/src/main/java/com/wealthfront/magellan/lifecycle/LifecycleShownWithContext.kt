package com.wealthfront.magellan.lifecycle

import android.content.Context

class LifecycleShownWithContext<V>(
  val supplier: (Context) -> V
) : LifecycleAware {

  var data: V? = null
    protected set

  override fun show(context: Context) {
    data = supplier(context)
  }

  override fun hide(context: Context) {
    data = null
  }
}

class LifecycleCreatedWithContext<V>(
  val supplier: (Context) -> V
) : LifecycleAware {

  var data: V? = null
    protected set

  override fun create(context: Context) {
    data = supplier(context)
  }

  override fun hide(context: Context) {
    data = null
  }
}

fun <V> LifecycleOwner.lifecycleShownWithContext(supplier: (Context) -> V) =
  lifecycle(LifecycleShownWithContext(supplier), { it.data })

fun <V> LifecycleOwner.lifecycleCreatedWithContext(supplier: (Context) -> V) =
  lifecycle(LifecycleCreatedWithContext(supplier), { it.data })
