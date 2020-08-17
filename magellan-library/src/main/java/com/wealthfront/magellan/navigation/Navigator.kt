package com.wealthfront.magellan.navigation

interface Navigator {

  val journey: NavigableCompat

  val backStack: List<NavigationEvent>
}
