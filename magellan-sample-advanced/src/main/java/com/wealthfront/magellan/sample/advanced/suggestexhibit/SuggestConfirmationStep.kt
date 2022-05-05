package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.databinding.SuggestExhibitConfirmationBinding
import javax.inject.Inject

class SuggestConfirmationStep(
  private val suggestionQuotaReached: Boolean = false,
  private val finishFlow: () -> Unit
) : Step<SuggestExhibitConfirmationBinding>(SuggestExhibitConfirmationBinding::inflate) {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: SuggestExhibitConfirmationBinding) {
    toolbarHelper.setTitle("Thank you")
    binding.acknowledgeSubmitted.setOnClickListener { finishFlow() }
    val submissionMessage = if (suggestionQuotaReached) {
      context.getString(R.string.suggest_exhibit_quota_reached)
    } else {
      context.getString(R.string.suggest_exhibit_success)
    }
    binding.submissionMessage.text = submissionMessage
  }
}
