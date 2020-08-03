package com.wealthfront.magellan.sample

import android.app.AlertDialog
import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
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

  override fun onShow(context: Context, binding: DetailBinding) {
    binding.dialog.setOnClickListener {
      dialogComponent.showDialog { getDialog(context) }
    }
    binding.nextJourney.setOnClickListener {
      startSecondJourney()
    }
  }

  private fun getDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Are you sure about this?")
      .create()
  }
}
