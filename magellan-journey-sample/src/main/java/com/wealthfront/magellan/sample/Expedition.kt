package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.navigation.NavigationTraverser
import com.wealthfront.magellan.sample.databinding.ExpeditionBinding

class Expedition : Journey<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  private val navigationTraverser = NavigationTraverser(this)
  private val navListener by lifecycle(LoggingNavigableListener(navigationTraverser))

  override fun onCreate(context: Context) {
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  private fun goToSecondJourney() {
    navigator.show(SecondJourney())
  }
}
