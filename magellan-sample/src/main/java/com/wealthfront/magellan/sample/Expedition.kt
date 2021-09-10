package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.attachLateinitFieldToLifecycle
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.ExpeditionBinding
import com.wealthfront.magellan.transitions.ShowTransition
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Expedition : Journey<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  @set:Inject var navListener: LoggingNavigableListener by attachLateinitFieldToLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  private fun goToSecondJourney() {
    navigator.goTo(SecondJourney(), ShowTransition())
  }
}
