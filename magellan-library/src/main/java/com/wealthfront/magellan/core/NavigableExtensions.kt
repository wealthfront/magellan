package com.wealthfront.magellan.core

import com.wealthfront.magellan.lifecycle.LifecycleOwner

fun Navigable.childNavigables(): Set<Navigable> {
  val children = mutableSetOf<Navigable>()
  (this as? LifecycleOwner)?.children?.mapNotNull { it as? Navigable }?.forEach {
    children.add(it)
  }
  return children
}
