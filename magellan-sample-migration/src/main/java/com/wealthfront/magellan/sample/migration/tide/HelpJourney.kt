package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import com.wealthfront.magellan.core.SimpleJourney

class HelpJourney : SimpleJourney() {

  override fun onCreate(context: Context) {
    navigator.goTo(HelpScreen { navigator.goTo(DogBreedsStep()) })
  }
}
