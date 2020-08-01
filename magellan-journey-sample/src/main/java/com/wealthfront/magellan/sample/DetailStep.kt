package com.wealthfront.magellan.sample

import android.app.AlertDialog
import android.content.Context
import android.view.Menu
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
import com.wealthfront.magellan.sample.tools.Toaster
import com.wealthfront.magellan.view.DialogComponent
import javax.inject.Inject

class DetailStep(
  private val startSecondJourney: () -> Unit
) : Step<DetailBinding>(DetailBinding::inflate) {

  @Inject lateinit var toaster: Toaster

  @set:Inject var dialogComponent: DialogComponent by lateinitLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
  }

  override fun onUpdateMenu(menu: Menu) {
    menu.findItem(R.id.reset)
      .setVisible(true)
      .setOnMenuItemClickListener {
        startSecondJourney()
        return@setOnMenuItemClickListener true
      }
    menu.findItem(R.id.why)
      .setVisible(true)
      .setOnMenuItemClickListener {
        dialogComponent.showDialog(::getDialog)
        return@setOnMenuItemClickListener true
      }
  }

  private fun getDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Are you sure about this?")
      .create()
  }
}
