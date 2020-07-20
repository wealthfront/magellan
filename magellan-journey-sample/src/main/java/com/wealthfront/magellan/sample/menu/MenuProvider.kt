package com.wealthfront.magellan.sample.menu

import android.view.Menu
import android.view.MenuItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuProvider @Inject constructor() {

  private var menu: Menu? = null

  fun setMenu(menu: Menu?) {
    this.menu = menu
    updateMenu()
  }

  fun clearMenu() {
    menu = null
  }

  fun findItem(item: Int): MenuItem {
    return menu!!.findItem(item)
  }

  fun updateMenu() {
    if (menu != null) {
      for (i in 0 until menu!!.size()) {
        menu!!.getItem(i).isVisible = false
      }
    }
  }
}
