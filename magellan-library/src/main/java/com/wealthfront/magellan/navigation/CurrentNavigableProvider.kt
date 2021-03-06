package com.wealthfront.magellan.navigation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class CurrentNavigableProvider @Inject constructor() : NavigableListener {

  public lateinit var navigable: NavigableCompat

  public fun isCurrentNavigable(other: NavigableCompat): Boolean = navigable == other

  override fun onNavigableShown(navigable: NavigableCompat) {
    this.navigable = navigable
  }
}
