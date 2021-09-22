package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.OrderTicketsBasketBinding
import com.wealthfront.magellan.core.Step
import java.math.BigDecimal

val adultTicketCost: BigDecimal = BigDecimal("8.50")
val childTicketCost: BigDecimal = BigDecimal("5.00")

interface BasketStepListener {

  fun onBasketFilled(adultTicketCount: Int, childTicketCount: Int)
}

class OrderTicketsBasketStep(
  private val basketStepListener: BasketStepListener,
  private val ticketOrder: TicketOrder
) : Step<OrderTicketsBasketBinding>(OrderTicketsBasketBinding::inflate) {

  override fun onShow(context: Context, binding: OrderTicketsBasketBinding) {
    binding.adultTicketPriceLabel.text =
      context.getString(R.string.order_tickets_price_label, adultTicketCost.toPlainString())
    binding.childTicketPriceLabel.text =
      context.getString(R.string.order_tickets_price_label, childTicketCost.toPlainString())

    binding.adultTicketCount.setText(ticketOrder.adultTickets.toString())
    binding.adultTicketCount.doAfterTextChanged { updateTotalCost(context, binding) }
    binding.childTicketCount.setText(ticketOrder.childTickets.toString())
    binding.childTicketCount.doAfterTextChanged { updateTotalCost(context, binding) }
    updateTotalCost(context, binding)

    binding.next.setOnClickListener {
      basketStepListener.onBasketFilled(
        getTicketCount(binding.adultTicketCount),
        getTicketCount(binding.childTicketCount)
      )
    }
  }

  private fun getTicketCount(entryView: EditText): Int {
    val ticketCountText = entryView.text.toString()
    return if (ticketCountText.isNotBlank()) {
      ticketCountText.toInt()
    } else {
      0
    }
  }

  private fun updateTotalCost(context: Context, binding: OrderTicketsBasketBinding) {
    val adultTicketCount = getTicketCount(binding.adultTicketCount)
    val childTicketCount = getTicketCount(binding.childTicketCount)
    val totalCost = getTotalCost(adultTicketCount, childTicketCount)
    binding.totalTicketCount.text =
      context.getString(R.string.order_tickets_total_price, totalCost.toPlainString())
    binding.next.isEnabled = totalCost > BigDecimal.ZERO
  }

  private fun getTotalCost(adultTicketCount: Int, childTicketCount: Int): BigDecimal {
    val totalAdultTicketCost = (adultTicketCost * BigDecimal(adultTicketCount))
    val totalChildTicketCost = (childTicketCost * BigDecimal(childTicketCount))
    return totalAdultTicketCost + totalChildTicketCost
  }
}