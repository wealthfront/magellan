package com.wealthfront.magellan.lifecycle

import kotlin.reflect.KProperty

public fun <Child : LifecycleAware, Property> LifecycleOwner.attachFieldToLifecycle(lifecycleAware: Child, getter: (Child) -> Property): LifecycleChild<Child, Property> {
  return LifecycleChild(
    this,
    lifecycleAware,
    getter
  )
}

public fun <Child : LifecycleAware> LifecycleOwner.attachFieldToLifecycle(lifecycleAware: Child): LifecycleChild<Child, Child> {
  return LifecycleChild(
    this,
    lifecycleAware,
    { lifecycleAware }
  )
}

public class LifecycleChild<Child : LifecycleAware, Property>(
  parent: LifecycleOwner,
  private var lifecycleAware: Child,
  public val valueGetter: (Child) -> Property
) {

  private var overrideValue: Property? = null

  init {
    parent.attachToLifecycle(lifecycleAware)
  }

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): Property {
    return overrideValue ?: valueGetter(lifecycleAware)
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Property) {
    overrideValue = value
  }
}

public fun <Child : LifecycleAware> LifecycleOwner.attachLateinitFieldToLifecycle(): LateinitLifecycleChild<Child> {
  return LateinitLifecycleChild(this)
}

public class LateinitLifecycleChild<Child : LifecycleAware>(private val parent: LifecycleOwner) {

  private var lifecycleAware: Child? = null

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): Child {
    return lifecycleAware ?: error(
      "This lateinit LifecycleAware has not been set yet. (Has your dependency injection run yet?)"
    )
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Child) {
    if (value != lifecycleAware) {
      if (lifecycleAware != null) {
        parent.removeFromLifecycle(lifecycleAware!!)
      }
      lifecycleAware = value
      parent.attachToLifecycle(lifecycleAware!!)
    }
  }
}
