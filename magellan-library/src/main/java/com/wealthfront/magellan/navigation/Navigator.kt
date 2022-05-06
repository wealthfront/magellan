package com.wealthfront.magellan.navigation

import android.content.Context
import android.view.View

public interface ViewTemplateApplier {
  public fun onViewCreated(context: Context, view: View): View
}

public interface Navigator {

  public val backStack: List<NavigationEvent>

  public fun goBack(): Boolean
}
