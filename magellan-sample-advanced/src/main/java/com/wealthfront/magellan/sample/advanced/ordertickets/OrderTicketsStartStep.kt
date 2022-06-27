package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.databinding.OrderTicketsStartBinding

class OrderTicketsStartStep(private val goToOrderTickets: () -> Unit) :
  Step<OrderTicketsStartBinding>(OrderTicketsStartBinding::inflate) {

  override fun onShow(context: Context, binding: OrderTicketsStartBinding) {
    binding.orderTicketsStart.setOnClickListener { goToOrderTickets() }
  }
}
