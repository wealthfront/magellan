package com.wealthfront.magellan.sample

import android.content.Context
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.LegacyJourney
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding
import com.wealthfront.magellan.transitions.CircularRevealTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import javax.inject.Inject

class SecondJourney : LegacyJourney<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(DetailStep(::startSecondJourney, ::startDialogStep))
  }

  private fun startSecondJourney(clickedView: View) {
    startSecondJourney(LearnMoreStep(), CircularRevealTransition(clickedView))
  }

  private fun startSecondJourney(next: Navigable, magellanTransition: MagellanTransition) {
    navigator.navigate(
      { history ->
        history.removeAll { it !is FirstJourney }
        if (history.isEmpty()) {
          history.push(FirstJourney({}))
        }
        history.push(next)
      },
      magellanTransition,
      Direction.FORWARD,
    )
  }

  private fun startDialogStep() {
    navigator.goTo(DialogStep())
  }
}
