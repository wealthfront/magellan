package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.OpenForMocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForMocking
public class CurrentNavigableProvider @Inject constructor() : NavigableListener {

  public var navigable: NavigableCompat? = null

  public fun isCurrentNavigable(other: NavigableCompat): Boolean = navigable == other

  override fun onNavigableShown(navigable: NavigableCompat) {
    this.navigable = navigable
  }
}
