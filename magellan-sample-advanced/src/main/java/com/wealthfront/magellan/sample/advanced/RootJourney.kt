package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.advanced.cerealcollection.BrowseCollectionJourney
import com.wealthfront.magellan.sample.advanced.databinding.RootJourneyBinding
import com.wealthfront.magellan.sample.advanced.designcereal.DesignCerealJourney
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsJourney
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitJourney

class RootJourney :
  Journey<RootJourneyBinding>(RootJourneyBinding::inflate, RootJourneyBinding::rootJourney) {

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

  private fun goToRequestExhibit() {
    navigator.goTo(SuggestExhibitJourney { navigator.goBack() })
  }
}
