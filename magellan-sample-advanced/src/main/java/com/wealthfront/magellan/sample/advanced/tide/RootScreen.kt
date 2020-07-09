package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.ViewGroup
import com.wealthfront.magellan.compose.core.Screen
import com.wealthfront.magellan.compose.core.Navigator
import com.wealthfront.magellan.compose.lifecycle.lifecycle
import com.wealthfront.magellan.sample.advanced.R

class RootScreen : Screen(R.layout.base) {

  var navigator by lifecycle(Navigator { view as ViewGroup })

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