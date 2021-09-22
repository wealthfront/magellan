package com.wealthfront.magellan.sample.advanced.paymentinfo

import android.app.AlertDialog
import android.content.Context
import androidx.core.widget.doAfterTextChanged
import com.wealthfront.magellan.sample.advanced.databinding.PaymentMethodCreditCardBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.view.DialogComponent

class PaymentMethodCreditCardDetailsStep(private val onComplete: (PaymentMethod.CreditCard) -> Unit) :
  Step<PaymentMethodCreditCardBinding>(PaymentMethodCreditCardBinding::inflate) {

  var dialogComponent: DialogComponent by attachFieldToLifecycle(DialogComponent())

  override fun onShow(context: Context, binding: PaymentMethodCreditCardBinding) {
    binding.creditCardNumber.doAfterTextChanged {
      binding.done.isEnabled = it.toString().isNotBlank()
    }

    binding.done.setOnClickListener {
      val creditCardNumber = binding.creditCardNumber.text.toString()
      if (creditCardNumber.length < 16) {
        dialogComponent.showDialog { activity ->
          AlertDialog.Builder(activity)
            .setTitle("Invalid payment details")
            .setMessage("Please enter a 16-digit card number")
            .create()
        }
        return@setOnClickListener
      }

      val creditCard = PaymentMethod.CreditCard(creditCardNumber)
      onComplete(creditCard)
    }
  }
}