package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.databinding.MagellanRootBinding
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentInfoJourney
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentMethod
import com.wealthfront.magellan.transitions.DefaultTransition
import javax.inject.Inject

class OrderTicketsJourney(
  private val onOrderComplete: () -> Unit
) : SimpleJourney(), BasketStepListener {

  @Inject lateinit var toolbarHelper: ToolbarHelper
  private var ticketOrder = TicketOrder(0, 0)

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.goTo(OrderTicketsBasketStep(this, ticketOrder))
  }

  override fun onShow(context: Context, binding: MagellanRootBinding) {
    toolbarHelper.setTitle("Order tickets")
    toolbarHelper.showToolbar()
  }

  override fun onBasketFilled(adultTicketCount: Int, childTicketCount: Int) {
    ticketOrder = TicketOrder(adultTicketCount, childTicketCount)
    navigator.goTo(PaymentInfoJourney(ticketOrder, this::paymentMethodCollected))
  }

  private fun onTicketsOrdered() {
    val successStep = OrderTicketsSuccessStep(onOrderComplete)
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.clear()
      val next = NavigationEvent(successStep, DefaultTransition())
      backstack.push(next)
      next
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
      nextEvent
    }
  }
}

data class TicketOrder(
  val adultTickets: Int,
  val childTickets: Int,
  val paymentMethod: PaymentMethod? = null
)
