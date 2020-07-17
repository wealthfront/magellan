package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.sample.databinding.IntroBinding

internal class IntroScreen(
  private val goToLearnMore: () -> Unit
) : Screen<IntroBinding>(IntroBinding::inflate) {

  override fun onShow(context: Context, binding: IntroBinding) {
    binding.learnMore.setOnClickListener {
      goToLearnMore()
    }
  }
}
