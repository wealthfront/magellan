package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

object NavigationPropagator {

  private var listeners: List<NavigableListener> = mutableListOf()

  fun addNavigableListener(stepNavigationListener: NavigableListener) {
    listeners = listeners + stepNavigationListener
  }

  fun removeNavigableListener(navigableListener: NavigableListener) {
    listeners = listeners - navigableListener
  }

  fun onNavigate() {
    listeners.forEach {
      it.onNavigate()
    }
  }

  fun hideCurrentNavigable(currentNavigable: Navigable) {
    listeners.forEach {
      it.onNavigableHidden(currentNavigable)
    }
  }

  fun showCurrentNavigable(currentNavigable: Navigable) {
    listeners.forEach {
      it.onNavigableShown(currentNavigable)
    }
  }
}
