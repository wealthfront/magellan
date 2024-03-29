package com.wealthfront.magellan.sample

import android.content.Context
import android.view.View
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding
import com.wealthfront.magellan.transitions.CircularRevealTransition
import javax.inject.Inject

class SecondJourney : Journey<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(DetailStep(::startSecondJourney, ::startDialogStep))
  }

  private fun startSecondJourney(clickedView: View) {
    navigator.goTo(LearnMoreStep(), CircularRevealTransition(clickedView))
  }

  private fun startDialogStep() {
    navigator.goTo(DialogStep())
  }
}
