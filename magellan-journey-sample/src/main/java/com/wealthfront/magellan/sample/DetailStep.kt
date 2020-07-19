package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
import com.wealthfront.magellan.sample.menu.MenuProvider
import com.wealthfront.magellan.sample.tools.Toaster
import javax.inject.Inject

class DetailStep(
  private val startSecondJourney: () -> Unit
) : Step<DetailBinding>(DetailBinding::inflate) {

  @set:Inject var menuProvider: MenuProvider by lateinitLifecycle()
  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    val menu = menuProvider.findItem(R.id.activities)

      menu
      .setVisible(true)
      .setOnMenuItemClickListener {
        toaster.showToast("Menu item activities clicked!")
        startSecondJourney()
        return@setOnMenuItemClickListener true
      }
  }
}
