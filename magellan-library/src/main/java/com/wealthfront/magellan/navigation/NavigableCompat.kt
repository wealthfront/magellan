package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleAware

public interface NavigableCompat : LifecycleAware, Displayable {
  public val currentNavigable: NavigableCompat get() = this
  public fun createSnapshot(): NavigationNode = LeafNode(this)
}
