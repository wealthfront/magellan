package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Navigable

interface NavListener {

  fun onNavigate(navigable: Navigable?)
}