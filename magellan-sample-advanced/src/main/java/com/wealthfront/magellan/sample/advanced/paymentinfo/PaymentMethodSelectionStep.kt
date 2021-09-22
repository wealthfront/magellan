package com.wealthfront.magellan.sample.advanced.paymentinfo

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.PaymentMethodMenuBinding

class PaymentMethodSelectionStep(
  private val paymentMethodJourney: PaymentInfoJourney
) : Step<PaymentMethodMenuBinding>(PaymentMethodMenuBinding::inflate) {

  private lateinit var creditCardSelected: () -> Unit

  override fun onShow(context: Context, binding: PaymentMethodMenuBinding) {
    when (paymentMethodJourney.paymentMethod) {
      is PaymentMethod.CreditCard -> {
        val cardNumber =
          (paymentMethodJourney.paymentMethod!! as PaymentMethod.CreditCard).cardNumber
        binding.creditCard.text = "Use existing card $cardNumber"
        creditCardSelected = {
          paymentMethodJourney.paymentMethodCollected(paymentMethodJourney.paymentMethod!!)
        }
      }
      null -> {
        binding.creditCard.text = "Add new credit card"
        creditCardSelected = {
          binding.paymentMethodMenu.clearCheck()
          paymentMethodJourney.goToCreditCardDetails()
        }
      }
    }

    binding.paymentMethodMenu.setOnCheckedChangeListener { group, checkedId ->
      if (checkedId == R.id.creditCard) {
        creditCardSelected()
      }
    }
  }
}
