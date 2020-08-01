package com.wealthfront.magellan.sample

import android.content.Context
import android.view.Menu
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.SecondJourneyBinding
import com.wealthfront.magellan.sample.tools.Toaster
import javax.inject.Inject

class SecondJourney : Journey<SecondJourneyBinding>(SecondJourneyBinding::inflate, SecondJourneyBinding::container) {

  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(DetailStep(::startSecondJourney))
  }

  override fun onUpdateMenu(menu: Menu) {
    menu.findItem(R.id.notifications)
      .setVisible(true)
      .setOnMenuItemClickListener {
        toaster.showToast("Menu item notifications clicked!")
        return@setOnMenuItemClickListener true
      }
  }

  private fun startSecondJourney() {
    navigator.goTo(LearnMoreStep())
  }
}
