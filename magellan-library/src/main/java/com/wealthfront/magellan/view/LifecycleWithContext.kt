package com.wealthfront.magellan.view

import android.content.Context
import com.wealthfront.magellan.Mockable
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import com.wealthfront.magellan.lifecycle.lifecycle

@Mockable
class LifecycleWithContext<V>(
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
