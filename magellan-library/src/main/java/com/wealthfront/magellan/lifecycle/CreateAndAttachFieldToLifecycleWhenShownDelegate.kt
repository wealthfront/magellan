package com.wealthfront.magellan.lifecycle

import android.content.Context

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
