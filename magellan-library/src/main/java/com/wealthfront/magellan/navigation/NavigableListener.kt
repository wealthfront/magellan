package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface NavigableListener : LifecycleAware {

  fun onNavigate() {}

  fun onNavigableShown(navigable: NavigableCompat) {}

  fun onNavigableHidden(navigable: NavigableCompat) {}

  override fun create(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun destroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }
}
