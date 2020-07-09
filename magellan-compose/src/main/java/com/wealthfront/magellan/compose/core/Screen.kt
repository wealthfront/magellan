package com.wealthfront.magellan.compose.core

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.wealthfront.magellan.compose.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.compose.view.lifecycleView
import com.wealthfront.magellan.compose.view.Displayable

abstract class Screen(
  @LayoutRes val layoutRes: Int
) : Displayable, LifecycleAwareComponent() {

  final override var view: View? by lifecycleView { context -> View.inflate(context, layoutRes, null) }
    private set

  final override fun onShow(context: Context) {
    onShow(context, view!!)
  }

  final override fun onResume(context: Context) {
    onResume(context, view!!)
  }

  final override fun onPause(context: Context) {
    onPause(context, view!!)
  }

  final override fun onHide(context: Context) {
    onHide(context, view!!)
  }

  protected open fun onShow(context: Context, view: View) {}

  protected open fun onResume(context: Context, view: View) {}

  protected open fun onPause(context: Context, view: View) {}

  protected open fun onHide(context: Context, view: View) {}

}