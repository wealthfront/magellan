package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface NavigableListener : LifecycleAware {

  fun onNavigate() {}

  fun onNavigableShown(navigable: NavigableCompat) {}

  fun onNavigableHidden(navigable: NavigableCompat) {}

  @JvmDefault
  override fun create(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  @JvmDefault
  override fun destroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }
}
