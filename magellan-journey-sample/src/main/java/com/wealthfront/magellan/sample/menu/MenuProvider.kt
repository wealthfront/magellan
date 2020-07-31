package com.wealthfront.magellan.sample.menu

import android.content.Context
import android.view.Menu
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.navigation.NavigableListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuProvider @Inject constructor() : NavigableListener {

  private var menu: Menu? = null
  private var currentNavigable: Navigable? = null

  fun setMenu(menu: Menu?) {
    this.menu = menu
    hideAllMenuItems()
    callUpdateMenuOnNavigable()
  }

  override fun onNavigableShown(navigable: Navigable) {
    hideAllMenuItems()
    currentNavigable = navigable
    callUpdateMenuOnNavigable()
  }

  override fun destroy(context: Context) {
    clearMenu()
  }

  private fun callUpdateMenuOnNavigable() {
    if (menu != null) {
      currentNavigable?.onUpdateMenu(menu!!)
    }
  }

  private fun clearMenu() {
    menu = null
    currentNavigable = null
  }

  private fun hideAllMenuItems() {
    if (menu != null) {
      for (i in 0 until menu!!.size()) {
        menu!!.getItem(i).isVisible = false
      }
    }
  }
}
