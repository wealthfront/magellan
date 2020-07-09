package com.wealthfront.magellan.compose.core

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import com.wealthfront.magellan.compose.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.compose.view.Displayable
import com.wealthfront.magellan.compose.view.lifecycleView

private const val DEFAULT_ACTION_BAR_COLOR_RES = 0

abstract class Screen(
  @LayoutRes val layoutRes: Int
) : Displayable, LifecycleAwareComponent() {

  private var viewState: SparseArray<Parcelable>? = null

  final override var view: View? by lifecycleView { context -> View.inflate(context, layoutRes, null) }
    private set

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
  }

  private fun saveViewState() {
    viewState = SparseArray()
    view!!.saveHierarchyState(viewState)
    viewState = null
  }

  open fun shouldShowActionBar(): Boolean = true

  open fun shouldAnimateActionBar(): Boolean = true

  @ColorRes
  open fun getActionBarColorRes(): Int = DEFAULT_ACTION_BAR_COLOR_RES

  protected open fun onShow(context: Context, view: View) {}

  protected open fun onResume(context: Context, view: View) {}

  protected open fun onPause(context: Context, view: View) {}

  protected open fun onHide(context: Context, view: View) {}

}