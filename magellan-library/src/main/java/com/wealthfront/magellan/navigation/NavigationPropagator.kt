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

  fun hideCurrentNavigable(currentNavigable: NavigationItem) {
    listeners.forEach {
      it.onNavigableHidden(currentNavigable)
    }
  }

  fun showCurrentNavigable(currentNavigable: NavigationItem) {
    listeners.forEach {
      it.onNavigableShown(currentNavigable)
    }
  }
}
