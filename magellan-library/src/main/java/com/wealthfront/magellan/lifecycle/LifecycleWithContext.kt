package com.wealthfront.magellan.lifecycle

import android.content.Context

public class LifecycleWithContext<V>(
  public val supplier: (Context) -> V
) : LifecycleAware {

  public var data: V? = null
    protected set

  override fun show(context: Context) {
    data = supplier(context)
  }

  override fun hide(context: Context) {
    data = null
  }
}

public fun <V> LifecycleOwner.lifecycleWithContext(supplier: (Context) -> V): Lifecycle<LifecycleWithContext<V>, V?> =
  lifecycle(LifecycleWithContext(supplier), { it.data })
