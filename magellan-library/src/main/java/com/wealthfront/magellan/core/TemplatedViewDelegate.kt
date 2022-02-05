package com.wealthfront.magellan.core

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

// can we attach a child of the view property? But we need a delegate that doesn't detach whenever override happens
// no, view isn't a LifecycleOwner. Can't attach to it. see UnsafeAttachFieldToLifecycleDelegate
//public class TemplatedViewDelegate<ChildType : LifecycleAware, PropertyType>(
//  parent: LifecycleOwner,
//  lifecycleAware: ChildType,
//  public val fieldSupplier: (Context) -> PropertyType
//) : LifecycleAware {
//
//  public var field: PropertyType? = null
//    protected set
//
//  init {
//    parent.attachToLifecycle(lifecycleAware)
//  }
//
//  public operator fun getValue(thisRef: Any?, property: KProperty<*>): PropertyType {
//    return field
//  }
//
//  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PropertyType) {
//
//  }
//  override fun show(context: Context) {
//    field = fieldSupplier(context)
//  }
//
//  override fun hide(context: Context) {
//    field = null
//  }
//}

//public class TemplatedViewDelegate<ChildType : LifecycleAware, PropertyType>(
//  private val parent: LifecycleOwner,
//  private val lifecycleAware: ChildType,
//  private val getPropertyType: (ChildType) -> PropertyType,
//) {
//
//  private var overrideValue: PropertyType? = null
//
//
//
//  @Suppress("UNCHECKED_CAST")
//  public operator fun getValue(thisRef: Any?, property: KProperty<*>): PropertyType {
//    return if (hasOverrideValue) {
//      overrideValue as PropertyType
//    } else {
//      getPropertyType(lifecycleAware)
//    }
//  }
//
//  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PropertyType) {
//    // I'm pretty sure this is why we're not tracking templatedView by lifecycle
//    if (!hasOverrideValue) {
//      parent.removeFromLifecycle(lifecycleAware)
//    }
//    hasOverrideValue = true
//    if (overrideValue != value) {
//      if (overrideValue is LifecycleAware) {
//        parent.removeFromLifecycle(overrideValue as LifecycleAware)
//      }
//      overrideValue = value
//      if (value is LifecycleAware) {
//        parent.attachToLifecycle(value)
//      }
//    }
//  }
//}