package com.wealthfront.magellan.core

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.lifecycle.createAndAttachFieldToLifecycleWhenShown
import kotlinx.coroutines.CoroutineScope

public abstract class Step<V : ViewBinding>(
  inflateBinding: (LayoutInflater) -> V
) : Navigable, LifecycleAwareComponent() {

  private var viewState: SparseArray<Parcelable>? = null

  public var viewBinding: V? by createAndAttachFieldToLifecycleWhenShown { context -> inflateBinding.invoke(LayoutInflater.from(context)) }
    @VisibleForTesting set

  final override var view: View? by createAndAttachFieldToLifecycleWhenShown { viewBinding!!.root }
    @VisibleForTesting set

  public var shownScope: LifecycleAware by attachFieldToLifecycle(Magellan.shownScopeProvider())
    @VisibleForTesting set

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
    viewState = null
  }

  private fun saveViewState() {
    viewState = SparseArray()
    view!!.saveHierarchyState(viewState)
  }

  protected open fun onShow(context: Context, binding: V) {}

  protected open fun onResume(context: Context, binding: V) {}

  protected open fun onPause(context: Context, binding: V) {}

  protected open fun onHide(context: Context, binding: V) {}
}
