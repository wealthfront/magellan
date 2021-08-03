package com.wealthfront.magellan.sample

import android.content.Context
import com.example.magellan.compose.ComposeJourney
import com.example.magellan.compose.ComposeStepWrappingViewStep
import com.example.magellan.compose.transitions.showTransition
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.App.Provider.appComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Expedition : ComposeJourney() {

  @set:Inject var navListener: LoggingNavigableListener by lateinitLifecycle()

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  private fun goToSecondJourney() {
    navigator.goTo(ComposeStepWrappingViewStep(SecondJourney()), showTransition)
  }
}
