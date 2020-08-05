package com.wealthfront.magellan.sample.advanced.tide

import android.app.AlertDialog
import android.content.Context
import com.wealthfront.magellan.Screen

class HelpScreen : Screen<HelpView>() {

  override fun createView(context: Context): HelpView {
    return HelpView(context)
  }

  fun showHelpDialog() {
    showDialog(::getDialog)
  }

  private fun getDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Did you find what you were looking for?")
      .create()
  }
}
