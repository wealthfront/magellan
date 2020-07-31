package com.wealthfront.magellan.sample

import android.app.AlertDialog
import android.content.Context
import android.util.Log
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

  @set:Inject var menuProvider: MenuProvider by lateinitLifecycle()
  @set:Inject var dialogComponent: DialogComponent by lateinitLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
  }

  override fun onUpdateMenu(menu: Menu) {
    menu.findItem(R.id.notifications)
      .setVisible(true)
      .setOnMenuItemClickListener {
        Log.i(this::class.java.simpleName, "Menu item notifications clicked!")
        startSecondJourney()
        return@setOnMenuItemClickListener true
      }
    menuProvider.findItem(R.id.why)
      .setVisible(true)
      .setOnMenuItemClickListener {
        dialogComponent.showDialog(::getDialog)
        return@setOnMenuItemClickListener true
      }
  }

  private fun getDialog(context: Context): AlertDialog {
    val dialog = AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Are you sure about this?")
      .create()
    return dialog
  }
}
