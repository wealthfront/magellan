package com.wealthfront.magellan.navigation

public interface Navigator {

  public val journey: NavigableCompat

  public val backStack: List<NavigationEvent>
}
