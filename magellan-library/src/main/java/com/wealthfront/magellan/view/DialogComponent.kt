package com.wealthfront.magellan.view

import android.app.Dialog
import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import javax.inject.Inject

class DialogComponent @Inject constructor() : LifecycleAware {

  private var dialogCreator: ((Context) -> Dialog)? = null
  private var dialog: Dialog? = null
  private var context: Context? = null

  private var dialogIsShowing: Boolean = false

  fun showDialog(dialogCreator: (Context) -> Dialog) {
    this.dialogCreator = dialogCreator
    this.dialogIsShowing = true
    createDialog()
  }

  override fun create(context: Context) {
    this.context = context
  }

  override fun destroy(context: Context) {
    this.context = null
    destroyDialog()
  }

  private fun createDialog() {
    if (dialogCreator != null && context != null && dialogIsShowing) {
      dialog = dialogCreator!!.invoke(context!!)
      dialog!!.show()
    }
  }

  private fun destroyDialog() {
    if (dialog != null) {
      dialogIsShowing = dialog!!.isShowing
      dialog!!.setOnDismissListener(null)
      dialog!!.dismiss()
      dialog = null
    }
  }
}