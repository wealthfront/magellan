package com.ryanmoelter.magellanx.core.lifecycle

import kotlin.reflect.KProperty

public fun <ChildType : LifecycleAware, PropertyType> LifecycleOwner.attachFieldToLifecycle(
  lifecycleAware: ChildType,
  getPropertyType: (ChildType) -> PropertyType
): AttachFieldToLifecycleDelegate<ChildType, PropertyType> {
  return AttachFieldToLifecycleDelegate(
    parent = this,
    lifecycleAware = lifecycleAware,
    getPropertyType = getPropertyType
  )
}

public fun <ChildType : LifecycleAware> LifecycleOwner.attachFieldToLifecycle(
  lifecycleAware: ChildType
): AttachFieldToLifecycleDelegate<ChildType, ChildType> {
  return AttachFieldToLifecycleDelegate(
    parent = this,
    lifecycleAware = lifecycleAware,
    getPropertyType = { lifecycleAware }
  )
}

public class AttachFieldToLifecycleDelegate<ChildType : LifecycleAware, PropertyType>(
  private val parent: LifecycleOwner,
  private val lifecycleAware: ChildType,
  private val getPropertyType: (ChildType) -> PropertyType
) {

  private var hasOverrideValue = false
  private var overrideValue: PropertyType? = null

  init {
    parent.attachToLifecycle(lifecycleAware)
  }

  @Suppress("UNCHECKED_CAST")
  public operator fun getValue(thisRef: Any?, property: KProperty<*>): PropertyType {
    return if (hasOverrideValue) {
      overrideValue as PropertyType
    } else {
      getPropertyType(lifecycleAware)
    }
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PropertyType) {
    if (!hasOverrideValue) {
      parent.removeFromLifecycle(lifecycleAware)
    }
    hasOverrideValue = true
    if (overrideValue != value) {
      if (overrideValue is LifecycleAware) {
        parent.removeFromLifecycle(overrideValue as LifecycleAware)
      }
      overrideValue = value
      if (value is LifecycleAware) {
        parent.attachToLifecycle(value)
      }
    }
  }
}

public fun <ChildType : LifecycleAware> LifecycleOwner.attachLateinitFieldToLifecycle():
  AttachLateinitFieldToLifecycleDelegate<ChildType> {
  return AttachLateinitFieldToLifecycleDelegate(this)
}

public class AttachLateinitFieldToLifecycleDelegate<ChildType : LifecycleAware>(
  private val parent: LifecycleOwner
) {

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
