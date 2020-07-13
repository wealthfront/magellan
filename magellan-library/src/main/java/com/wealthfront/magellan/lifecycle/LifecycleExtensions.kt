package com.wealthfront.magellan.lifecycle

import kotlin.reflect.KProperty

fun <T : LifecycleAware, R> LifecycleOwner.lifecycle(lifecycleAware: T, getter: (T) -> R): Lifecycle<T, R> {
  return Lifecycle(
    this,
    lifecycleAware,
    getter
  )
}

fun <T : LifecycleAware> LifecycleOwner.lifecycle(lifecycleAware: T): Lifecycle<T, T> {
  return Lifecycle(
    this,
    lifecycleAware,
    { lifecycleAware })
}

class Lifecycle<T : LifecycleAware, R>(parent: LifecycleOwner, lifecycleAware: T, val getter: (T) -> R) {

  var lifecycleAware = lifecycleAware
    private set

  var overrideValue: R? = null

  init {
    parent.attachToLifecycle(lifecycleAware)
  }

  operator fun getValue(thisRef: Any?, property: KProperty<*>): R {
    return overrideValue ?: getter(lifecycleAware)
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
    overrideValue = value
  }
}

fun <CustomLifecycleAware : LifecycleAware> LifecycleOwner.lateinitLifecycle(): LateinitLifecycle<CustomLifecycleAware> {
  return LateinitLifecycle(this)
}

class LateinitLifecycle<CustomLifecycleAware : LifecycleAware>(val parent: LifecycleOwner) {

  lateinit var lifecycleAware: CustomLifecycleAware
    private set

  operator fun getValue(thisRef: Any?, property: KProperty<*>): CustomLifecycleAware {
    return lifecycleAware
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: CustomLifecycleAware) {
    lifecycleAware = value
    parent.attachToLifecycle(lifecycleAware)
  }
}