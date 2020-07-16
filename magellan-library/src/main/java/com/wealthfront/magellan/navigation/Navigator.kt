package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

interface Navigator {

  val backStack: List<Navigable>
}
