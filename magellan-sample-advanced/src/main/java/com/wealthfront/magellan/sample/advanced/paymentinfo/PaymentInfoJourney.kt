package com.wealthfront.magellan.sample.advanced.paymentinfo

import android.content.Context
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.PaymentMethodBinding
import com.wealthfront.magellan.sample.advanced.ordertickets.TicketOrder
import com.wealthfront.magellan.core.Journey

class PaymentInfoJourney(
  ticketOrder: TicketOrder,
  private val onPaymentMethodCollected: (PaymentMethod) -> Unit
) : Journey<PaymentMethodBinding>(
  PaymentMethodBinding::inflate,
  PaymentMethodBinding::paymentMethodContainer
) {

  var paymentMethod: PaymentMethod? = ticketOrder.paymentMethod
    private set

  override fun onCreate(context: Context) {
    navigator.goTo(PaymentMethodSelectionStep(this))
  }

  override fun onShow(context: Context, binding: PaymentMethodBinding) {
    ToolbarHelperProvider.toolbarHelper.setTitle("Payment method")
    ToolbarHelperProvider.toolbarHelper.showToolbar()
  }

  fun goToCreditCardDetails() {
    navigator.goTo(PaymentMethodCreditCardDetailsStep(this::paymentMethodCollected))
  }

  fun paymentMethodCollected(method: PaymentMethod) {
    paymentMethod = method
    onPaymentMethodCollected(method)
  }
}

sealed class PaymentMethod {
  data class CreditCard(val cardNumber: String) : PaymentMethod()
}