package com.wealthfront.magellan.lifecycle

import android.content.Context

open class LifecycleWithContext<V>(
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

fun <V> LifecycleOwner.lifecycleWithContext(supplier: (Context) -> V) =
  lifecycle(LifecycleWithContext(supplier), { it.data })
