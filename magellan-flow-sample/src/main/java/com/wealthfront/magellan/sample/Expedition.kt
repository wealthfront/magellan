package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.databinding.ExpeditionBinding

class Expedition : Journey<ExpeditionBinding>(ExpeditionBinding::inflate, ExpeditionBinding::container) {

  override fun onCreate(context: Context) {
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  private fun goToSecondJourney() {
    navigator.show(SecondJourney())
  }
}