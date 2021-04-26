package com.wealthfront.magellan.lifecycle

import kotlin.reflect.KProperty

public fun <T : LifecycleAware, R> LifecycleOwner.lifecycle(lifecycleAware: T, getter: (T) -> R): Lifecycle<T, R> {
  return Lifecycle(
    this,
    lifecycleAware,
    getter
  )
}

public fun <T : LifecycleAware> LifecycleOwner.lifecycle(lifecycleAware: T): Lifecycle<T, T> {
  return Lifecycle(
    this,
    lifecycleAware,
    { lifecycleAware }
  )
}

public class Lifecycle<T : LifecycleAware, R>(
  parent: LifecycleOwner,
  private var lifecycleAware: T,
  public val getter: (T) -> R
) {

  private var overrideValue: R? = null

  init {
    parent.attachToLifecycle(lifecycleAware)
  }

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): R {
    return overrideValue ?: getter(lifecycleAware)
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
    overrideValue = value
  }
}

public fun <CustomLifecycleAware : LifecycleAware> LifecycleOwner.lateinitLifecycle(): LateinitLifecycle<CustomLifecycleAware> {
  return LateinitLifecycle(this)
}

public class LateinitLifecycle<CustomLifecycleAware : LifecycleAware>(private val parent: LifecycleOwner) {

  private var lifecycleAware: CustomLifecycleAware? = null

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): CustomLifecycleAware {
    return lifecycleAware ?: error(
      "This lateinit LifecycleAware has not been set yet. (Has your dependency injection run yet?)"
    )
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: CustomLifecycleAware) {
    if (value != lifecycleAware) {
      if (lifecycleAware != null) {
        parent.removeFromLifecycle(lifecycleAware!!)
      }
      lifecycleAware = value
      parent.attachToLifecycle(lifecycleAware!!)
    }
  }
}
