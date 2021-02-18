package com.wealthfront.magellan.sample.advanced.toolbar

import android.content.Context
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigableListener

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

  override fun onNavigableHidden(navigable: NavigableCompat) {
    toolbarView?.reset()
  }

  override fun destroy(context: Context) {
    toolbarView = null
  }
}
