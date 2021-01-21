package com.wealthfront.magellan.navigation

import java.util.Deque

public interface Navigator {

  public val journey: NavigableCompat

  public val backStack: Deque<NavigationEvent>
}
