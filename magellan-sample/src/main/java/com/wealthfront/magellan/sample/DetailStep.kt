package com.wealthfront.magellan.sample

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.attachLateinitFieldToLifecycle
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
import com.wealthfront.magellan.view.DialogComponent
import javax.inject.Inject

class DetailStep(
  private val startSecondJourney: (clickedView: View) -> Unit,
  private val startDialogStep: () -> Unit
) : Step<DetailBinding>(DetailBinding::inflate) {

  @set:Inject var dialogComponent: DialogComponent by attachLateinitFieldToLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
  }

  override fun onShow(context: Context, binding: DetailBinding) {
    binding.dialog1.setOnClickListener {
      dialogComponent.showDialog(DialogCreator { activity -> getHelloDialog(activity) })
    }

    binding.dialog2.setOnClickListener {
      dialogComponent.showDialog(DialogCreator { activity -> getOrderDialog(activity) })
    }
    binding.dialog3.setOnClickListener {
      startDialogStep()
    }
    binding.detailNextJourney.setOnClickListener {
      startSecondJourney(it)
    }
  }

  private fun getHelloDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Hello")
      .setMessage("Are you sure about this?")
      .create()
  }

  private fun getOrderDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context)
      .setTitle("Can I take your order?")
      .setMessage("Would you like fries with that?")
      .setPositiveButton(R.string.response_affirmative, null)
      .setNegativeButton(R.string.response_negative, null)
      .create()
  }
}
