package com.wealthfront.magellan.navigation

object NavigationPropagator {

  private var listeners: Set<NavigableListener> = emptySet()

  fun addNavigableListener(navigableListener: NavigableListener) {
    listeners = listeners + navigableListener
  }

  fun removeNavigableListener(navigableListener: NavigableListener) {
    listeners = listeners - navigableListener
  }

  fun onNavigate() {
    listeners.forEach {
      it.onNavigate()
    }
  }

  fun hideCurrentNavigable(currentNavigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigableHidden(currentNavigable)
    }
  }

  fun showCurrentNavigable(currentNavigable: NavigableCompat) {
    listeners.forEach {
      it.onNavigableShown(currentNavigable)
    }
  }
}
