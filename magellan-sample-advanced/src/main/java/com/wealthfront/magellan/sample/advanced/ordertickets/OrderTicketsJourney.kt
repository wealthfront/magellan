package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentInfoJourney
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentMethod
import com.wealthfront.magellan.transitions.DefaultTransition

class OrderTicketsJourney : SimpleJourney(), BasketStepListener {

  private lateinit var ticketOrder: TicketOrder

  override fun onCreate(context: Context) = startOrder()

  private fun startOrder() {
    ticketOrder = TicketOrder(0, 0)
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.clear()
      val next = NavigationEvent(OrderTicketsBasketStep(this, ticketOrder), DefaultTransition())
      backstack.push(next)
      next.magellanTransition
    }
  }

  override fun onBasketFilled(adultTicketCount: Int, childTicketCount: Int) {
    ticketOrder = TicketOrder(adultTicketCount, childTicketCount)
    navigator.goTo(PaymentInfoJourney(ticketOrder, this::paymentMethodCollected))
  }

  private fun onTicketsOrdered() {
    val successStep = OrderTicketsSuccessStep(this::startOrder)
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.clear()
      val next = NavigationEvent(successStep, DefaultTransition())
      backstack.push(next)
      next.magellanTransition
    }
  }

  private fun paymentMethodCollected(method: PaymentMethod) {
    ticketOrder = ticketOrder.copy(paymentMethod = method)
    val confirmationStep = OrderTicketsConfirmationStep(ticketOrder, this::onTicketsOrdered)
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.forEach { backstackEntry ->
        if (backstackEntry.navigable is PaymentInfoJourney) {
          backstack.remove(backstackEntry)
          val paymentInfoJourney = PaymentInfoJourney(ticketOrder, this::paymentMethodCollected)
          backstack.push(NavigationEvent(paymentInfoJourney, DefaultTransition()))
        }
      }
      val nextEvent = NavigationEvent(confirmationStep, DefaultTransition())
      backstack.push(nextEvent)
      nextEvent.magellanTransition
    }
  }
}

data class TicketOrder(
  val adultTickets: Int,
  val childTickets: Int,
  val paymentMethod: PaymentMethod? = null
)
