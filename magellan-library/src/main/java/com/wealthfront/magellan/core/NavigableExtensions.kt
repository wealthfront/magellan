package com.wealthfront.magellan.core

import com.wealthfront.magellan.lifecycle.LifecycleOwner
import com.wealthfront.magellan.navigation.NavigableCompat

public fun NavigableCompat.childNavigables(): Set<NavigableCompat> {
  val children = mutableSetOf<NavigableCompat>()
  (this as? LifecycleOwner)?.children?.mapNotNull { it as? NavigableCompat }?.forEach {
    children.add(it)
  }
  return children
}
