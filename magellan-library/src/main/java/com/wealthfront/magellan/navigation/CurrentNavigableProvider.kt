package com.wealthfront.magellan.navigation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentNavigableProvider @Inject constructor() : NavigableListener {

  lateinit var navigable: NavigableCompat

  fun isCurrentNavigable(other: NavigableCompat) = navigable == other

  override fun onNavigableShown(navigable: NavigableCompat) {
    this.navigable = navigable
  }
}
