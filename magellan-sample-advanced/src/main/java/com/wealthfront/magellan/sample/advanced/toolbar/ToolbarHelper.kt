package com.wealthfront.magellan.sample.advanced.toolbar

import android.content.Context
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigableListener

/**
 * [ToolbarHelper] provides APIs to modify the toolbar for the activity.
 *
 * Ideally, this dependency is provider by dependency injection and configured so that the views are cleaned up when the activity
 * is destroyed with the help of (subcomponents & custom scopes)[https://dagger.dev/dev-guide/subcomponents].
 */
object ToolbarHelper : NavigableListener {

  private var toolbarView: ToolbarView? = null

  fun init(toolbarView: ToolbarView, navigator: Navigator) {
    this.toolbarView = toolbarView
    toolbarView.binding.back.setOnClickListener {
      navigator.goBack()
    }
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

  override fun onNavigatedFrom(navigable: NavigableCompat) {
    toolbarView?.reset()
  }

  override fun destroy(context: Context) {
    toolbarView = null
  }
}
