package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.databinding.FirstJourneyBinding

class FirstJourney(
  private val secondJourney: () -> Unit
) : Journey<FirstJourneyBinding>(FirstJourneyBinding::inflate, FirstJourneyBinding::container) {

  override fun onCreate(context: Context) {
    navigator.goTo(IntroScreen(::goToLearnMore))
  }

  override fun onShow(context: Context, binding: FirstJourneyBinding) {
    binding.nextFlow.setOnClickListener {
      secondJourney()
    }
  }

  private fun goToLearnMore() {
    navigator.goTo(LearnMoreScreen())
  }
}
