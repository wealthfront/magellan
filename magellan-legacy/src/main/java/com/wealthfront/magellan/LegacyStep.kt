package com.wealthfront.magellan

import android.content.Context
import android.view.View
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.lifecycle.createAndAttachFieldToLifecycleWhenShown
import com.wealthfront.magellan.view.DialogComponent

public abstract class LegacyStep<V : View> : LifecycleAwareComponent(), Navigable {

  protected val dialogComponent: DialogComponent by attachFieldToLifecycle(DialogComponent())

  override var view: V? by createAndAttachFieldToLifecycleWhenShown { context ->
    createView(context)
  }
    @VisibleForTesting set

  protected abstract fun createView(context: Context): V
}
