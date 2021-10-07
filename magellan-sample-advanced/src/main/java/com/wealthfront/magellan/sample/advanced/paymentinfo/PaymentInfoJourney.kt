package com.wealthfront.magellan.sample.advanced.paymentinfo

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.databinding.PaymentMethodBinding
import com.wealthfront.magellan.sample.advanced.ordertickets.TicketOrder
import javax.inject.Inject

class PaymentInfoJourney(
  ticketOrder: TicketOrder,
  private val onPaymentMethodCollected: (PaymentMethod) -> Unit
) : Journey<PaymentMethodBinding>(
  PaymentMethodBinding::inflate,
  PaymentMethodBinding::paymentMethodContainer
) {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  var paymentMethod: PaymentMethod? = ticketOrder.paymentMethod
    private set

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.goTo(PaymentMethodSelectionStep(this))
  }

  override fun onShow(context: Context, binding: PaymentMethodBinding) {
    toolbarHelper.setTitle("Payment method")
    toolbarHelper.showToolbar()
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
