package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.LinearNavigator
import com.wealthfront.magellan.sample.databinding.FirstJourneyBinding

class FirstJourney(
  private val goToSecondJourney: () -> Unit
) : Journey<FirstJourneyBinding>(FirstJourneyBinding::inflate, FirstJourneyBinding::container) {

  protected var navigator1 by lifecycle(LinearNavigator { view as ScreenContainer })

  override fun onCreate(context: Context) {
    navigator.goTo(IntroStep(::goToLearnMore))
  }

  override fun onShow(context: Context, binding: FirstJourneyBinding) {
    binding.nextJourney.setOnClickListener {
      goToSecondJourney()
    }
  }

  private fun goToLearnMore() {
    navigator.goTo(LearnMoreStep())
  }
}
