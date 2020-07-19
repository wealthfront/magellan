package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

interface NavigableListener {

  fun onNavigate() {}

  fun onNavigableShown(navigable: Navigable) {}

  fun onNavigableHidden(navigable: Navigable) {}
}
