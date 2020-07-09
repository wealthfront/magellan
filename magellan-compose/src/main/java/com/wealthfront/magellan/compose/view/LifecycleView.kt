package com.wealthfront.magellan.compose.view

import android.content.Context
import android.view.View
import com.wealthfront.magellan.compose.lifecycle.LifecycleAware
import com.wealthfront.magellan.compose.lifecycle.LifecycleOwner
import com.wealthfront.magellan.compose.lifecycle.lifecycle

open class LifecycleView<CustomView : View>(
  val createView: (Context) -> CustomView
) : LifecycleAware {
  var view: CustomView? = null
    protected set

  override fun show(context: Context) {
    view = createView(context)
  }

  override fun hide(context: Context) {
    view = null
  }
}

fun <CustomView : View> LifecycleOwner.lifecycleView(createView: (Context) -> CustomView) =
  lifecycle(LifecycleView(createView), { it.view })