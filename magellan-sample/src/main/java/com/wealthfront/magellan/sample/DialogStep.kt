package com.wealthfront.magellan.sample

import android.app.AlertDialog
import android.content.Context
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.sample.databinding.EmptyBinding
import com.wealthfront.magellan.view.DialogComponent

class DialogStep : Step<EmptyBinding>(EmptyBinding::inflate) {

  private val dialogComponent by attachFieldToLifecycle(DialogComponent())

  override fun onShow(context: Context, binding: EmptyBinding) {
    dialogComponent.showDialog(DialogCreator { activity -> getHelloDialog(activity) })
  }

  private fun getHelloDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Are you sure about this?")
      .create()
  }
}
