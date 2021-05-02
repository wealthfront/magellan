package com.wealthfront.magellan.navigation

public interface NavigationListener {

  public fun onNavigatedTo(navigable: NavigableCompat<*>) {}

  public fun onNavigatedFrom(navigable: NavigableCompat<*>) {}

  public fun beforeNavigation() {}

  public fun afterNavigation() {}
}
