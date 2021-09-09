package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.databinding.IntroBinding

internal class IntroStep(
  private val goToLearnMore: () -> Unit
) : Step<IntroBinding>(IntroBinding::inflate) {

  override fun onStart(context: Context, binding: IntroBinding) {
    binding.learnMore.setOnClickListener {
      goToLearnMore()
    }
  }
}
