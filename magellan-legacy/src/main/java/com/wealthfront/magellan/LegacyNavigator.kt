package com.wealthfront.magellan

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Looper.getMainLooper
import android.view.Menu
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import java.util.Stack

class LegacyNavigator internal constructor(
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))
  private var activity: Activity? = null
  internal var menu: Menu? = null
    set(value) {
      updateMenu(menu)
      field = value
    }

  override val backStack: Stack<NavigationEvent>
    get() = delegate.backStack

  init {
    delegate.currentNavigableSetup = { navItem ->
      if (navItem is Screen<*>) {
        navItem.setNavigator(this)
        navItem.setActivity(activity!!)
        navItem.setTitle(navItem.getTitle(activity!!))
        callOnNavigate(navItem)
      }
      updateMenu(menu, navItem)
    }
  }

  private fun updateMenu(menu: Menu?, navItem: NavigableCompat? = null) {
    // Need to post to avoid animation bug on disappearing menu
    Handler(getMainLooper()).post {
      menu?.let {
        for (i in 0 until menu.size()) {
          menu.getItem(i).isVisible = false
          if (navItem is Screen<*>) {
            navItem.onUpdateMenu(menu)
          }
        }
      }
    }
  }

  private fun callOnNavigate(currentScreen: Screen<*>) {
    (activity as? NavigationListener)?.onNavigate(
      ActionBarConfig.with()
        .visible(currentScreen.shouldShowActionBar())
        .animated(currentScreen.shouldAnimateActionBar())
        .colorRes(currentScreen.getActionBarColorRes())
        .build())
  }

  override fun onCreate(context: Context) {
    this.activity = context as Activity
  }

  override fun onDestroy(context: Context) {
    menu = null
    activity = null
  }

  fun goTo(navigable: NavigableCompat) {
    delegate.goTo(navigable)
  }

  fun show(navigable: NavigableCompat) {
    delegate.show(navigable)
  }

  fun replaceAndGo(navigable: NavigableCompat) {
    delegate.replaceAndGo(navigable)
  }

  fun replaceAndShow(navigable: NavigableCompat) {
    delegate.replaceAndShow(navigable)
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  fun goBack(): Boolean {
    return delegate.goBack()
  }
}
