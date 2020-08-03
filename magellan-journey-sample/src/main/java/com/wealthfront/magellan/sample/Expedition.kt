package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.ExpeditionBinding
import javax.inject.Inject

class Expedition : Journey<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  @set:Inject var navListener: LoggingNavigableListener by lateinitLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  private fun goToSecondJourney() {
    navigator.show(SecondJourney())
  }
}
