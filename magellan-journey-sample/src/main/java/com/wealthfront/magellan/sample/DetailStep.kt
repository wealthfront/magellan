package com.wealthfront.magellan.sample

import android.content.Context
import android.view.Menu
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
import com.wealthfront.magellan.sample.tools.Toaster
import javax.inject.Inject

class DetailStep(
  private val startSecondJourney: () -> Unit
) : Step<DetailBinding>(DetailBinding::inflate) {

  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
  }

  override fun onUpdateMenu(menu: Menu) {
    menu.findItem(R.id.reset)
      .setVisible(true)
      .setOnMenuItemClickListener {
        startSecondJourney()
        return@setOnMenuItemClickListener true
      }
  }
}
