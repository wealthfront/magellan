package com.wealthfront.magellan.sample.advanced.toolbar

import android.content.Context
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import kotlinx.coroutines.launch

/**
 * [ToolbarHelper] provides APIs to modify the toolbar for the activity.
 *
 * Ideally, this dependency is provided by dependency injection and configured so that the views are cleaned up when the activity
 * is destroyed with the help of (subcomponents & custom scopes)[https://dagger.dev/dev-guide/subcomponents].
 */
object ToolbarHelper : LifecycleAwareComponent() {

  private val shownScope by attachFieldToLifecycle(ShownLifecycleScope())
  private var toolbarView: ToolbarView? = null

  fun init(toolbarView: ToolbarView, navigator: Navigator) {
    this.toolbarView = toolbarView
    toolbarView.binding.back.setOnClickListener {
      navigator.goBack()
    }
  }

  override fun onStart(context: Context) {
    shownScope.launch {
      NavigationPropagator.events
        .filterIsInstance<NavigationLifecycleEvent.NavigatedFrom>()
        .collect { event ->
          toolbarView?.reset()
        }
    }
  }

  override fun onStop(context: Context) {
    toolbarView = null
  }

  fun setTitle(title: CharSequence) {
    toolbarView!!.setTitle(title)
  }

  fun setMenuColor(color: Int) {
    toolbarView!!.setMenuColor(color)
  }

  fun setMenuIcon(icon: Int, onClickListener: () -> Unit) {
    toolbarView!!.setMenuIcon(icon, onClickListener)
  }
}
