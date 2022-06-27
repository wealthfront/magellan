package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.designcereal.DesignCerealJourney
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsJourney
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestConfirmationStep
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitJourney

class RootJourney : SimpleJourney() {

  private val designCerealJourney = DesignCerealJourney(this::designCerealComplete)
  private val orderTicketsJourney = OrderTicketsJourney(this::orderTicketsComplete)
  private val suggestExhibitJourney = SuggestExhibitJourney(this::requestExhibitComplete)
  private var alreadyRequestedExhibit = false

  override fun onCreate(context: Context) {
    val mainStep = MainMenuStep(
      this::goToDesignCereal,
      this::goToOrderTickets,
      this::goToSuggestExhibit
    )
    navigator.goTo(mainStep)
  }

  private fun goToDesignCereal() {
    navigator.goTo(designCerealJourney)
  }

  private fun goToOrderTickets() {
    navigator.goTo(orderTicketsJourney)
  }

  private fun goToSuggestExhibit() {
    navigator.navigate(Direction.FORWARD) { backStack ->
      if (alreadyRequestedExhibit) {
        backStack.push(
          NavigationEvent(
            SuggestConfirmationStep(true, this::requestExhibitComplete),
            getDefaultTransition()
          )
        )
      } else {
        alreadyRequestedExhibit = true
        backStack.push(
          NavigationEvent(
            suggestExhibitJourney,
            getDefaultTransition()
          )
        )
      }
      backStack.peek()!!.magellanTransition
    }
  }

  private fun designCerealComplete() {
    navigator.goBack()
  }

  private fun orderTicketsComplete() {
    navigator.goBack()
  }

  private fun requestExhibitComplete() {
    navigator.goBack()
  }
}
