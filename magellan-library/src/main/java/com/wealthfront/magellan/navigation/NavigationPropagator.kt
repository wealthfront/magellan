package com.wealthfront.magellan.navigation

public object NavigationPropagator {

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

  public fun duringNavigation() {
    listeners.forEach {
      it.duringNavigation()
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
}
