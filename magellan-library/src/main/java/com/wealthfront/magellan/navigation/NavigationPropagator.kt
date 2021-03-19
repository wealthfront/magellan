package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware

public object NavigationPropagator : LifecycleAware {

  private var listeners: Set<NavigableListener> = emptySet()

  @JvmStatic
  public fun addNavigableListener(navigableListener: NavigableListener) {
    listeners = listeners + navigableListener
  }

  @JvmStatic
  public fun removeNavigableListener(navigableListener: NavigableListener) {
    listeners = listeners - navigableListener
  }

  public fun beforeNavigation() {
    listeners.forEach {
      it.beforeNavigation()
    }
  }

  public fun afterNavigation() {
    listeners.forEach {
      it.afterNavigation()
    }
  }

  public fun hideCurrentNavigable(currentNavigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigableHidden(currentNavigable)
    }
  }

  public fun showCurrentNavigable(currentNavigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigableShown(currentNavigable)
    }
  }

  override fun destroy(context: Context?) {
    listeners = emptySet()
  }
}
