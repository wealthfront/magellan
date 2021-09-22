package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.SuggestExhibitConfirmationBinding

class SuggestConfirmationStep(
  private val finishFlow: () -> Unit
) : Step<SuggestExhibitConfirmationBinding>(SuggestExhibitConfirmationBinding::inflate) {

  override fun onShow(context: Context, binding: SuggestExhibitConfirmationBinding) {
    binding.acknowledgeSubmitted.setOnClickListener { finishFlow() }
    ToolbarHelperProvider.toolbarHelper.setTitle("Thank you")
  }
}
