package com.wealthfront.magellan.navigation

import android.view.View

public interface NavigationRequestHandler {
  public fun onNavigationRequested(
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  ): Boolean
}

public interface ViewTemplateApplier {
  public fun onViewCreated(view: View): View
}

public interface Navigator {

  public val backStack: List<NavigationEvent>

  public fun goBack(): Boolean
}
