package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAware

interface NavigableListener : LifecycleAware {

  fun onNavigate() {}

  fun onNavigableShown(navigable: Navigable) {}

  fun onNavigableHidden(navigable: Navigable) {}

  override fun create(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun destroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }
}
