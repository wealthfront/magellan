package com.wealthfront.magellan.navigation

public interface Navigator<ViewType : Any> {

  public val backStack: List<NavigationEvent<ViewType>>

  public fun goBack(): Boolean
}
