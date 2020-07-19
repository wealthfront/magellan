package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

object NavigationPropagator {

  private var listeners: List<StepNavigationListener> = mutableListOf()

  fun addStepNavigationListener(stepNavigationListener: StepNavigationListener) {
    listeners = listeners + stepNavigationListener
  }

  fun removeStepNavigationListener(stepNavigationListener: StepNavigationListener) {
    listeners = listeners - stepNavigationListener
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
