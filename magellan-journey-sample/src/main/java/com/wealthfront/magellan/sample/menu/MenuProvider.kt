package com.wealthfront.magellan.sample.menu

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.wealthfront.magellan.navigation.NavigableListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuProvider @Inject constructor() : NavigableListener {

  private var menu: Menu? = null

  fun setMenu(menu: Menu?) {
    this.menu = menu
    hideAllMenuItems()
  }

  override fun onNavigate() {
    hideAllMenuItems()
  }

  fun findItem(item: Int): MenuItem {
    return menu!!.findItem(item)
  }

  fun hideAllMenuItems() {
    if (menu != null) {
      for (i in 0 until menu!!.size()) {
        menu!!.getItem(i).isVisible = false
      }
    }
  }
}
