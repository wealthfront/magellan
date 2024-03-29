package com.wealthfront.magellan.sample.migration.toolbar

import android.content.Context
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.navigation.NavigationListener
import com.wealthfront.magellan.navigation.NavigationPropagator

/**
 * [ToolbarHelper] provides APIs to modify the toolbar for the activity.
 *
 * Ideally, this dependency is provider by dependency injection and configured so that the views are cleaned up when the activity
 * is destroyed with the help of (subcomponents & custom scopes)[https://dagger.dev/dev-guide/subcomponents].
 */
@OpenForMocking
class ToolbarHelper : LifecycleAwareComponent(), NavigationListener {

  private var toolbarView: ToolbarView? = null

  fun init(toolbarView: ToolbarView, navigator: Navigator) {
    this.toolbarView = toolbarView
    toolbarView.binding.back.setOnClickListener { navigator.goBack() }
  }

  override fun onShow(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun onHide(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
    this.toolbarView = null
  }

  override fun beforeNavigation() {
    toolbarView?.reset()
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
