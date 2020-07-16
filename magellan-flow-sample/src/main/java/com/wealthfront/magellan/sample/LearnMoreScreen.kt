package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.sample.databinding.LearnMoreBinding

internal class LearnMoreScreen(
  private val exitFlow: () -> Unit
) : Screen<LearnMoreBinding>(LearnMoreBinding::inflate) {

  override fun onShow(context: Context, binding: LearnMoreBinding) {
    binding.exitFlow.setOnClickListener {
      exitFlow()
    }
  }
}
