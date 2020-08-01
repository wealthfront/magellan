package com.wealthfront.magellan.view

import android.app.Dialog
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.lifecycle.LifecycleAware
import javax.inject.Inject

typealias DialogCreator = (Context) -> Dialog

class DialogComponent @Inject constructor() : LifecycleAware {

  private var dialogCreator: DialogCreator? = null
  private var dialog: Dialog? = null
  private var context: Context? = null

  @VisibleForTesting internal var dialogIsShowing: Boolean = false

  fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    createDialog()
  }

  override fun create(context: Context) {
    this.context = context
  }

  override fun show(context: Context) {
    createDialog()
  }

  override fun hide(context: Context) {
    destroyDialog()
  }

  override fun destroy(context: Context) {
    this.context = null
  }

  private fun createDialog() {
    if (dialogCreator != null && context != null) {
      dialog = dialogCreator!!.invoke(context!!)
      dialog!!.show()
      dialogIsShowing = true
    }
  }

  private fun destroyDialog() {
    if (dialog != null) {
      dialog!!.setOnDismissListener(null)
      dialog!!.dismiss()
      dialogIsShowing = false
      dialog = null
    }
  }
}
