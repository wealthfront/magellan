package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface NavigableListener : LifecycleAware {

  fun onNavigableShown(navigable: NavigableCompat) {}

  fun onNavigableHidden(navigable: NavigableCompat) {}

  @JvmDefault
  fun onNavigate() {}

  @JvmDefault
  override fun create(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  @JvmDefault
  override fun destroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }
}
