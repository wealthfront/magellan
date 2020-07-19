package com.wealthfront.magellan.sample.menu

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.sample.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuProvider @Inject constructor() : LifecycleAware {

  private var menu: Menu? = null

  override fun create(context: Context) {
    menu = (context as MainActivity).menu
  }

  override fun destroy(context: Context) {
    menu = null
  }

  fun findItem(item: Int): MenuItem {
    return menu!!.findItem(item)
  }
}
