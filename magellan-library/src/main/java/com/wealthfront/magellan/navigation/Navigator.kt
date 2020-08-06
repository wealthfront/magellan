package com.wealthfront.magellan.navigation

import java.util.Stack

interface Navigator {

  val backStack: Stack<NavigationItem>
}
