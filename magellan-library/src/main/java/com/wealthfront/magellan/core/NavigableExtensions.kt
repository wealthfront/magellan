package com.wealthfront.magellan.core

import com.wealthfront.magellan.lifecycle.LifecycleOwner

fun Navigable.childNavigables(): List<Navigable> {
  val children = mutableListOf<Navigable>()
  (this as? LifecycleOwner)?.children?.mapNotNull { it as? Navigable }?.forEach {
    children.add(it)
  }
  return children
}
