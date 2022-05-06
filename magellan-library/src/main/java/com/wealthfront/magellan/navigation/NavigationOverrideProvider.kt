package com.wealthfront.magellan.navigation

public interface NavigationOverrideProvider {

  public fun getNavigationOverrides(): List<NavigationOverride>
}

public data class NavigationOverride(
  val conditions: (
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  ) -> Boolean,
  val navigationOperation: (
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  ) -> Unit
)
