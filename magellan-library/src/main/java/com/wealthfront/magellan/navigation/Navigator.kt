package com.wealthfront.magellan.navigation

public interface Navigator {

  public val backStack: List<NavigationEvent>

  public fun goBack(): Boolean
}
