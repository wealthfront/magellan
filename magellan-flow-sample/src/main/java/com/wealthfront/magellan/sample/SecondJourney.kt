package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding

class SecondJourney :
  Journey<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  override fun onCreate(context: Context) {
    navigator.navigate(FORWARD) { stack ->
      stack.push(DetailScreen())
      stack.push(DetailScreen())
      stack.push(DetailScreen())
    }
  }

  override fun onShow(context: Context, binding: SecondJourneyBinding) {
    binding.goBackToFirstScreen.setOnClickListener {
      navigator.navigate(BACKWARD) { stack ->
        while (stack.size > 1) {
          stack.pop()
        }
      }
    }
  }
}
