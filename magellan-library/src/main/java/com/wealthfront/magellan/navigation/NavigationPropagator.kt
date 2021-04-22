package com.wealthfront.magellan.navigation

public object NavigationPropagator {

  private var listeners: Set<NavigationListener> = emptySet()

  @JvmStatic
  public fun addNavigableListener(navigationListener: NavigationListener) {
    listeners = listeners + navigationListener
  }

  @JvmStatic
  public fun removeNavigableListener(navigationListener: NavigationListener) {
    listeners = listeners - navigationListener
  }

  internal fun beforeNavigation() {
    listeners.forEach {
      it.beforeNavigation()
    }
  }

  internal fun afterNavigation() {
    listeners.forEach {
      it.afterNavigation()
    }
  }

  internal fun onNavigatedTo(navigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigatedTo(navigable)
    }
  }

  internal fun onNavigatedFrom(navigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigatedFrom(navigable)
    }
  }
}
