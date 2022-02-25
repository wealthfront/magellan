package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View

public interface NavigationRequestHandler {
  public fun shouldOverrideNavigation(
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  ): Boolean

  // what if this supplied a backstack operation, instead?
  public fun overrideNavigationRequest(
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  )
}

public interface ViewTemplateApplier {
  public fun onViewCreated(context: Context, view: View): View
}

public interface Navigator {

  public val backStack: List<NavigationEvent>

  public fun goBack(): Boolean
}
