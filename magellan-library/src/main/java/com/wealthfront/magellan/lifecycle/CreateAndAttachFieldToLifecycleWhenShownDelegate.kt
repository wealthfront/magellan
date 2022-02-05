package com.wealthfront.magellan.lifecycle

import android.content.Context
import kotlin.reflect.KProperty

public class CreateAndAttachFieldToLifecycleWhenShownDelegate<Field>(
  public val fieldSupplier: (Context) -> Field
) : LifecycleAware {

  public var field: Field? = null
    protected set

  override fun show(context: Context) {
    field = fieldSupplier(context)
  }

  override fun hide(context: Context) {
    field = null
  }
}

public fun <Field> LifecycleOwner.createAndAttachFieldToLifecycleWhenShown(fieldSupplier: (Context) -> Field): AttachFieldToLifecycleDelegate<CreateAndAttachFieldToLifecycleWhenShownDelegate<Field>, Field?> =
  attachFieldToLifecycle(CreateAndAttachFieldToLifecycleWhenShownDelegate(fieldSupplier), { it.field })


public class OverrideableShownFieldDelegate<PropertyType>(
  parent: LifecycleOwner,
  private val fieldSupplier: (Context) -> PropertyType,
): LifecycleAware {

  private var field: PropertyType? = null

  init {
    parent.attachToLifecycle(this)
  }

  override fun show(context: Context) {
    field = fieldSupplier(context)
  }

  override fun hide(context: Context) {
    field = null
  }

  @Suppress("UNCHECKED_CAST")
  public operator fun getValue(thisRef: Any?, property: KProperty<*>): PropertyType? {
    return field
  }

  public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: PropertyType) {
    field = value
  }
}

public fun <Field> LifecycleOwner.createAndAttachOverridableFieldToLifecycleWhenShown(fieldSupplier: (Context) -> Field): OverrideableShownFieldDelegate<Field> {
  return OverrideableShownFieldDelegate(
    parent = this,
    fieldSupplier = fieldSupplier
  )
}
