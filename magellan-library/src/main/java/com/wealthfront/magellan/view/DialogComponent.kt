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
  private var resumed = false
  public var dialog: Dialog? = null
    private set
  public var context: Context? = null

  @VisibleForTesting
  internal var shouldRestoreDialog: Boolean = false

  public fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    createDialogIfResumed()
  }

  public fun showDialog(dialogCreator: (Activity) -> Dialog) {
    this.dialogCreator = DialogCreator { dialogCreator.invoke(it) }
    createDialogIfResumed()
  }

  override fun create(context: Context) {
  }

  override fun resume(context: Context) {
    this.context = context
    resumed = true
    if (shouldRestoreDialog) {
      createDialog()
      shouldRestoreDialog = false
    }
  }

  override fun pause(context: Context) {
    this.context = null
    resumed = false
    destroyDialog()
  }

  override fun destroy(context: Context) {
    this.context = null
  }

  private fun createDialogIfResumed() {
    if (resumed) {
      createDialog()
    } else {
      shouldRestoreDialog = true
    }
  }

  private fun createDialog() {
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
