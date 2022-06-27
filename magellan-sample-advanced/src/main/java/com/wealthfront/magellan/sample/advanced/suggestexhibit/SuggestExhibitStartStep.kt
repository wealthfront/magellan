package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.databinding.SuggestExhibitStartBinding

class SuggestExhibitStartStep(private val goToSuggestExhibitJourney: () -> Unit) :
  Step<SuggestExhibitStartBinding>(SuggestExhibitStartBinding::inflate) {

  override fun onShow(context: Context, binding: SuggestExhibitStartBinding) {
    binding.suggestExhibitStart.setOnClickListener { goToSuggestExhibitJourney() }
  }
}
