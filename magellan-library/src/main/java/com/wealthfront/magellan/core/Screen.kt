package com.wealthfront.magellan.core

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.view.lifecycleView

abstract class Screen(
  @LayoutRes val layoutRes: Int
) : Navigable, LifecycleAwareComponent() {

  private var viewState: SparseArray<Parcelable>? = null

  final override var view: View? by lifecycleView { context ->  View.inflate(context, layoutRes, null) }
    @VisibleForTesting set

  final override fun onShow(context: Context) {
    restoreViewState()
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
    saveViewState()
  }

  private fun restoreViewState() {
    if (viewState != null) {
      view!!.restoreHierarchyState(viewState)
    }
    viewState = null
  }

  private fun saveViewState() {
    viewState = SparseArray()
    view!!.saveHierarchyState(viewState)
  }

  protected open fun onShow(context: Context, view: View) {}

  protected open fun onResume(context: Context, view: View) {}

  protected open fun onPause(context: Context, view: View) {}

  protected open fun onHide(context: Context, view: View) {}
}