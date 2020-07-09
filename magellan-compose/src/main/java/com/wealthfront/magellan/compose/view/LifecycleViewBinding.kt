package com.wealthfront.magellan.compose.view

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
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

open class LifecycleViewBinding<V : ViewBinding>(
  val createBinding: (Context) -> V
) : LifecycleAware {

  var binding: V? = null
    protected set

  override fun show(context: Context) {
    binding = createBinding(context)
  }

  override fun hide(context: Context) {
    binding = null
  }
}

fun <V : ViewBinding> LifecycleOwner.lifecycleBinding(createView: (Context) -> V) =
  lifecycle(LifecycleViewBinding(createView), { it.binding })


fun <V : View> LifecycleOwner.lifecycleView(createView: (Context) -> V) =
  lifecycle(LifecycleView(createView), { it.view })