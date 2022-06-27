package com.wealthfront.magellan.sample.advanced.paymentinfo

import android.content.Context
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.databinding.MagellanSimpleJourneyBinding
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.ordertickets.TicketOrder
import javax.inject.Inject

class PaymentInfoJourney(
  ticketOrder: TicketOrder,
  private val onPaymentMethodCollected: (PaymentMethod) -> Unit
) : SimpleJourney() {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  var paymentMethod: PaymentMethod? = ticketOrder.paymentMethod
    private set

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.goTo(PaymentMethodSelectionStep(this))
  }

  override fun onShow(context: Context, binding: MagellanSimpleJourneyBinding) {
    super.onShow(context, binding)
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
