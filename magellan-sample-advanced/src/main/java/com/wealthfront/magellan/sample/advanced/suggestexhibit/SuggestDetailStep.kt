package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.sample.advanced.databinding.SuggestExhibitDetailBinding
import com.wealthfront.magellan.core.Step

class SuggestDetailStep(private val goToConfirmation: () -> Unit) :
  Step<SuggestExhibitDetailBinding>(SuggestExhibitDetailBinding::inflate) {

  override fun onShow(context: Context, binding: SuggestExhibitDetailBinding) {
    binding.submitSuggestion.setOnClickListener { goToConfirmation() }
  }
}