package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.cerealcollection.BrowseCollectionJourney
import com.wealthfront.magellan.sample.advanced.designcereal.DesignCerealJourney
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsJourney
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestConfirmationStep
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitJourney

class RootJourney : SimpleJourney() {

  private var alreadyRequestedExhibit = false

  override fun onCreate(context: Context) {
    super.onCreate(context)
    val mainStep = MainMenuStep(
      this::goToBrowseCollection,
      this::goToDesignCereal,
      this::goToOrderTickets,
      this::goToRequestExhibit
    )
    navigator.goTo(mainStep)
  }

  private fun goToBrowseCollection() {
    navigator.goTo(BrowseCollectionJourney())
  }

  private fun goToDesignCereal() {
    navigator.goTo(DesignCerealJourney { navigator.goBack() })
  }

  private fun goToOrderTickets() {
    navigator.goTo(OrderTicketsJourney { navigator.goBack() })
  }

  // non-idempotent navigation operation
  private fun goToRequestExhibit() {
    val requestExhibitJourney = SuggestExhibitJourney { navigator.goBack() }
    navigator.navigate(Direction.FORWARD) { backStack ->
      if (alreadyRequestedExhibit) {
        backStack.push(
          NavigationEvent(
            SuggestConfirmationStep(true) { navigator.goBack() },
            getDefaultTransition()
          )
        )
      } else {
        alreadyRequestedExhibit = true
        backStack.push(
          NavigationEvent(
            requestExhibitJourney,
            getDefaultTransition()
          )
        )
      }
      backStack.peek()!!.magellanTransition
    }
  }
}
