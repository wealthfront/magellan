package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.ViewGroup
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.core.NavigationDirection
import com.wealthfront.magellan.core.Navigator
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.lifecycle.lifecycleView
import com.wealthfront.magellan.sample.advanced.R

class RootScreen : Screen(R.layout.base) {

  var navigator by lifecycle(Navigator { view as ViewGroup })

  override fun onCreate(context: Context) {
    goToTideLocationScreen()
  }

  private fun goToTideLocationScreen() {
    val onLocationSelected: (Int) -> Unit = { noaaApiId ->
      navigator.goTo(TideDetailsScreen(noaaApiId), NavigationDirection.FORWARD)
    }

    navigator.goTo(TideLocationsScreen(onLocationSelected))
  }
}