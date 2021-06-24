package com.wealthfront.magellan.sample

import android.content.Context
import com.example.magellan.compose.ComposeStepWrapper
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.FirstJourneyBinding
import javax.inject.Inject

class FirstJourney(
  private val goToSecondJourney: () -> Unit
) : Journey<FirstJourneyBinding>(FirstJourneyBinding::inflate, FirstJourneyBinding::container) {

  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(ComposeStepWrapper(IntroStep(::goToLearnMore)))
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
