package com.wealthfront.magellan.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.lifecycle.LifecycleAware
import javax.inject.Inject

public class DialogComponent @Inject constructor() : LifecycleAware {

  private var dialogCreator: DialogCreator? = null
  public var dialog: Dialog? = null
    private set
  private var context: Context? = null

  public var dialogIsShowing: Boolean = false

  public fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    this.dialogIsShowing = true
    createDialog()
  }

  public fun showDialog(dialogCreator: (Activity) -> Dialog) {
    this.dialogCreator = DialogCreator { dialogCreator.invoke(it) }
    this.dialogIsShowing = true
    createDialog()
  }

  override fun show(context: Context) {
    this.context = context
    createDialog()
  }

  override fun hide(context: Context) {
    destroyDialog()
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
