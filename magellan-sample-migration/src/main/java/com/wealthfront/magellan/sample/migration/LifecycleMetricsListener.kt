package com.wealthfront.magellan.sample.migration

import android.content.Context
import android.util.Log
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationListener
import com.wealthfront.magellan.navigation.NavigationPropagator
import org.joda.time.DateTime

private const val UNINITIALIZED = 0L

class LifecycleMetricsListener : NavigationListener, LifecycleAwareComponent() {

  private var screenShownTimestamp: Long = UNINITIALIZED
  private var shownScreen: NavigableCompat? = null

  override fun onCreate(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun onDestroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }

  override fun onNavigatedTo(navigable: NavigableCompat) {
    this.screenShownTimestamp = DateTime().millis
    this.shownScreen = navigable
    Log.v(shownScreen!!::class.simpleName, "view created")
  }

  override fun onNavigatedFrom(navigable: NavigableCompat) {
    val screenHiddenTimestamp = DateTime().millis
    if (screenShownTimestamp != UNINITIALIZED && shownScreen == navigable) {
      shownScreen = null
      val timeSpentOnScreen = screenHiddenTimestamp - screenShownTimestamp
      Log.v("screentime", timeSpentOnScreen.toString())
    }
    Log.v(navigable::class.simpleName, "view destroyed")
  }

  override fun afterNavigation() {
    Log.v("LifecycleMetrics", "navigation-animation-complete")
  }
}
