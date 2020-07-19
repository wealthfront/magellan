package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding
import com.wealthfront.magellan.sample.menu.MenuProvider
import com.wealthfront.magellan.sample.tools.Toaster
import javax.inject.Inject

class SecondJourney : Journey<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  @set:Inject var menuProvider: MenuProvider by lateinitLifecycle()
  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    menuProvider.findItem(R.id.activities)
      .setVisible(true)
      .setOnMenuItemClickListener {
        toaster.showToast("Menu item activities clicked!")
        return@setOnMenuItemClickListener true
      }
    navigator.goTo(DetailStep(::startSecondJourney))
  }

  private fun startSecondJourney() {
    navigator.goTo(SecondJourney())
  }
}
