package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.navigation.LinearNavigator
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.ExpeditionBinding
import com.wealthfront.magellan.sample.advanced.tide.DashboardStep
import com.wealthfront.magellan.sample.advanced.tide.DogDetailsScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Expedition @Inject constructor() : Journey<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  @set:Inject var navListener: LoggingNavigableListener by lateinitLifecycle()

  fun provideNavigator(): LinearNavigator {
    return navigator
  }

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.goTo(DashboardStep(::goToDetailsScreen))
  }

  private fun goToDetailsScreen(name: String) {
    navigator.goTo(DogDetailsScreen(name))
  }
}
