package com.ryanmoelter.magellanx.core

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner

@Suppress("UNCHECKED_CAST")
public fun <ViewType : Any> Navigable<ViewType>.childNavigables(): Set<Navigable<ViewType>> {
  return (this as? LifecycleOwner)?.children?.mapNotNull { it as? Navigable<ViewType> }?.toSet()
    ?: emptySet()
}
