package com.ryanmoelter.magellanx.core.lifecycle

import android.content.Context

public class CreateAndAttachFieldToLifecycleWhenShownDelegate<Field>(
  public val fieldSupplier: () -> Field
) : LifecycleAware {

  public var field: Field? = null
    protected set

  override fun show() {
    field = fieldSupplier()
  }

  override fun hide() {
    field = null
  }
}

public fun <Field> LifecycleOwner.createAndAttachFieldToLifecycleWhenShown(
  fieldSupplier: () -> Field
): AttachFieldToLifecycleDelegate<CreateAndAttachFieldToLifecycleWhenShownDelegate<Field>, Field?> =
  attachFieldToLifecycle(
    CreateAndAttachFieldToLifecycleWhenShownDelegate(fieldSupplier),
    { it.field }
  )
