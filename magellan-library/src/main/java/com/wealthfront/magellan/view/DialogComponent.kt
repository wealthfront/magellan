package com.wealthfront.magellan.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.lifecycle.LifecycleAware
import javax.inject.Inject

class DialogComponent @Inject constructor() : LifecycleAware {

  private var dialogCreator: DialogCreator? = null
  var dialog: Dialog? = null
    private set
  private var context: Context? = null

  var dialogIsShowing: Boolean = false

  fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    this.dialogIsShowing = true
    createDialog()
  }

  fun showDialog(dialogCreator: (Activity) -> Dialog) {
    this.dialogCreator = DialogCreator { dialogCreator.invoke(it) }
    this.dialogIsShowing = true
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
    if (dialogCreator != null && context != null && dialogIsShowing) {
      dialog = dialogCreator!!.createDialog(context as Activity)
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
