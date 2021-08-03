package com.wealthfront.magellan.navigation

import androidx.annotation.RestrictTo

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

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  public fun beforeNavigation() {
    listeners.forEach {
      it.beforeNavigation()
    }
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  public fun afterNavigation() {
    listeners.forEach {
      it.afterNavigation()
    }
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  public fun onNavigatedTo(navigable: NavigableCompat<*>) {
    listeners.forEach {
      it.onNavigatedTo(navigable)
    }
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
  public fun onNavigatedFrom(navigable: NavigableCompat<*>) {
    listeners.forEach {
      it.onNavigatedFrom(navigable)
    }
  }
}
