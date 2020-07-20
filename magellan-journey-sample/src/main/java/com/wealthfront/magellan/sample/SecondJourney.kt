package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding

class SecondJourney : Journey<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(DetailStep(::startSecondJourney))
  }

  private fun startSecondJourney() {
    navigator.goTo(LearnMoreStep())
  }
}
