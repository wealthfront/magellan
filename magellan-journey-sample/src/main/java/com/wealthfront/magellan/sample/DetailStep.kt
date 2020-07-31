package com.wealthfront.magellan.sample

import android.content.Context
import android.util.Log
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.sample.databinding.DetailBinding
import com.wealthfront.magellan.sample.menu.MenuProvider
import com.wealthfront.magellan.sample.tools.Toaster
import javax.inject.Inject

class DetailStep(
  private val startSecondJourney: () -> Unit
) : Step<DetailBinding>(DetailBinding::inflate) {

  @Inject lateinit var menuProvider: MenuProvider
  @Inject lateinit var toaster: Toaster

  override fun onCreate(context: Context) {
    appComponent.inject(this)
  }

  override fun onShow(context: Context, binding: DetailBinding) {
    menuProvider.findItem(R.id.notifications)
      .setVisible(true)
      .setOnMenuItemClickListener {
        Log.i(this::class.java.simpleName, "Menu item notifications clicked!")
        startSecondJourney()
        return@setOnMenuItemClickListener true
      }
  }
}
