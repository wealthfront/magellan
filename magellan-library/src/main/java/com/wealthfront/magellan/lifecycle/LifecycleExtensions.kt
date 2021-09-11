package com.wealthfront.magellan.lifecycle

import kotlin.reflect.KProperty

public fun <ChildType : LifecycleAware, PropertyType> LifecycleOwner.attachFieldToLifecycle(lifecycleAware: ChildType, getter: (ChildType) -> PropertyType): AttachFieldToLifecycleDelegate<ChildType, PropertyType> {
  return AttachFieldToLifecycleDelegate(
    this,
    lifecycleAware,
    getter
  )
}

public fun <ChildType : LifecycleAware> LifecycleOwner.attachFieldToLifecycle(lifecycleAware: ChildType): AttachFieldToLifecycleDelegate<ChildType, ChildType> {
  return AttachFieldToLifecycleDelegate(
    this,
    lifecycleAware,
    { lifecycleAware }
  )
}

public class AttachFieldToLifecycleDelegate<ChildType : LifecycleAware, PropertyType>(
  parent: LifecycleOwner,
  private var lifecycleAware: ChildType,
  public val valueGetter: (ChildType) -> PropertyType
) {

  private var overrideValue: PropertyType? = null

  init {
    parent.attachToLifecycle(lifecycleAware)
  }

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): PropertyType {
    return overrideValue ?: valueGetter(lifecycleAware)
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PropertyType) {
    overrideValue = value
  }
}

public fun <ChildType : LifecycleAware> LifecycleOwner.attachLateinitFieldToLifecycle(): AttachLateinitFieldToLifecycleDelegate<ChildType> {
  return AttachLateinitFieldToLifecycleDelegate(this)
}

public class AttachLateinitFieldToLifecycleDelegate<ChildType : LifecycleAware>(private val parent: LifecycleOwner) {

  private var lifecycleAware: ChildType? = null

  public operator fun getValue(thisRef: Any?, property: KProperty<*>): ChildType {
    return lifecycleAware ?: error(
      "This lateinit LifecycleAware has not been set yet. (Has your dependency injection run yet?)"
    )
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ChildType) {
    if (value != lifecycleAware) {
      if (lifecycleAware != null) {
        parent.removeFromLifecycle(lifecycleAware!!)
      }
      lifecycleAware = value
      parent.attachToLifecycle(lifecycleAware!!)
    }
  }
}
