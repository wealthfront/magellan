package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

interface StepNavigationListener {

  fun onNavigate() {}

  fun onNavigableShown(navigable: Navigable) {}

  fun onNavigableHidden(navigable: Navigable) {}
}
