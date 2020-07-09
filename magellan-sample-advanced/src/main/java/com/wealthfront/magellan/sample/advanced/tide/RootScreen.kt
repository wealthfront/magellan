package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.compose.core.Screen
import com.wealthfront.magellan.compose.core.Navigator
import com.wealthfront.magellan.compose.lifecycle.lifecycle
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.BaseBinding

class RootScreen : Screen<BaseBinding>(BaseBinding::inflate) {

  var navigator by lifecycle(Navigator { view as ScreenContainer })

  override fun onCreate(context: Context) {
    goToTideLocationScreen()
  }

  private fun goToTideLocationScreen() {
    val onLocationSelected: (Int) -> Unit = { noaaApiId ->
      navigator.goTo(TideDetailsScreen(noaaApiId))
    }

    navigator.goTo(TideLocationsScreen(onLocationSelected))
  }
}