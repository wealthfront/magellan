package com.wealthfront.magellan.compose.core

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.compose.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.compose.view.Displayable
import com.wealthfront.magellan.compose.view.lifecycleBinding
import com.wealthfront.magellan.compose.view.lifecycleView

abstract class Screen<V: ViewBinding>(
  createBinding: (LayoutInflater) -> V
) : Navigable, LifecycleAwareComponent() {

  private var viewState: SparseArray<Parcelable>? = null

  private val viewBinding: V? by lifecycleBinding { context ->  createBinding.invoke(LayoutInflater.from(context)) }

  final override var view: View? by lifecycleView { viewBinding!!.root }
    private set

  final override fun onShow(context: Context) {
    restoreViewState()
    onShow(context, viewBinding!!)
  }

  final override fun onResume(context: Context) {
    onResume(context, viewBinding!!)
  }

  final override fun onPause(context: Context) {
    onPause(context, viewBinding!!)
  }

  final override fun onHide(context: Context) {
    onHide(context, viewBinding!!)
    saveViewState()
  }

  private fun restoreViewState() {
    if (viewState != null) {
      view!!.restoreHierarchyState(viewState)
    }
  }

  private fun saveViewState() {
    viewState = SparseArray()
    view!!.saveHierarchyState(viewState)
    viewState = null
  }

  protected open fun onShow(context: Context, binding: V) {}

  protected open fun onResume(context: Context, binding: V) {}

  protected open fun onPause(context: Context, binding: V) {}

  protected open fun onHide(context: Context, binding: V) {}

}