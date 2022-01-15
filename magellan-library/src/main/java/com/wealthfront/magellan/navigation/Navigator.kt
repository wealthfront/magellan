package com.wealthfront.magellan.navigation

public interface NavigationRequestHandler {
  public fun onNavigationRequested(navigable: NavigableCompat): Boolean
}

public interface Navigator {

  public val backStack: List<NavigationEvent>

  public fun goBack(): Boolean
}
