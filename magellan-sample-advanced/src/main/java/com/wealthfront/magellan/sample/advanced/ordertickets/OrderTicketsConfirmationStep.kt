package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.OrderTicketsConfirmationBinding
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentMethod
import com.wealthfront.magellan.core.Step

class OrderTicketsConfirmationStep(
  private val ticketOrder: TicketOrder,
  private val onTicketsOrdered: () -> Unit
) : Step<OrderTicketsConfirmationBinding>(OrderTicketsConfirmationBinding::inflate) {

  override fun onShow(context: Context, binding: OrderTicketsConfirmationBinding) {
    ToolbarHelperProvider.toolbarHelper.setTitle("Confirm order")
    ToolbarHelperProvider.toolbarHelper.showToolbar()

    val adultTicketSummary = if (ticketOrder.adultTickets > 0) {
      context.resources.getQuantityString(
        R.plurals.order_tickets_order_summary,
        ticketOrder.adultTickets,
        ticketOrder.adultTickets,
        "Adult"
      )
    } else {
      null
    }

    val childTicketSummary = if (ticketOrder.childTickets > 0) {
      context.resources.getQuantityString(
        R.plurals.order_tickets_order_summary,
        ticketOrder.childTickets,
        ticketOrder.childTickets,
        "Child"
      )
    } else {
      null
    }
    binding.orderSummary.text = listOfNotNull(adultTicketSummary, childTicketSummary)
      .joinToString(separator = "\n")

    binding.paymentSummary.text = when (ticketOrder.paymentMethod!!) {
      is PaymentMethod.CreditCard -> {
        val cardNumber = (ticketOrder.paymentMethod as PaymentMethod.CreditCard).cardNumber
        "Credit card $cardNumber"
      }
    }
    binding.order.setOnClickListener { onTicketsOrdered() }
  }
}