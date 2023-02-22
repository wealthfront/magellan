package com.wealthfront.magellan.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.ContextUtil
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.lifecycle.LifecycleAware
import javax.inject.Inject

public class DialogComponent @Inject constructor() : LifecycleAware {

  private var dialogCreator: DialogCreator? = null
  public var dialog: Dialog? = null
    private set
  public var context: Context? = null

  @VisibleForTesting
  internal var shouldRestoreDialog: Boolean = false

  public fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    createDialog()
  }

  public fun showDialog(dialogCreator: (Activity) -> Dialog) {
    this.dialogCreator = DialogCreator { dialogCreator.invoke(it) }
    createDialog()
  }

  override fun show(context: Context) {
    this.context = context
  }

  override fun resume(context: Context) {
    if (shouldRestoreDialog) {
      createDialog()
      shouldRestoreDialog = false
    }
  }

  override fun pause(context: Context) {
    destroyDialog()
  }

  override fun hide(context: Context) {
    this.context = null
  }

  private fun createDialog() {
    shouldRestoreDialog = false
    val dialogCreator = dialogCreator
    val context = context
    if (dialogCreator != null && context != null) {
      val dialog = dialogCreator.createDialog(ContextUtil.findActivity(context))
      dialog!!.show()
      this.dialog = dialog
    }
  }

  private fun destroyDialog() {
    if (dialog != null) {
      shouldRestoreDialog = dialog!!.isShowing
      dialog!!.setOnDismissListener(null)
      dialog!!.dismiss()
      dialog = null
    }
  }
}
