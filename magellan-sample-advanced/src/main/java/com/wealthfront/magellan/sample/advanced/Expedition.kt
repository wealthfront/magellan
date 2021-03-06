package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.LegacyExpedition
import com.wealthfront.magellan.lifecycle.lateinitLifecycle
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.ExpeditionBinding
import com.wealthfront.magellan.sample.advanced.tide.DogDetailsScreen
import com.wealthfront.magellan.sample.advanced.tide.DogListStep
import com.wealthfront.magellan.sample.advanced.toolbar.ToolbarHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Expedition @Inject constructor() : LegacyExpedition<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  @set:Inject var navListener: LoggingNavigableListener by lateinitLifecycle()

  override fun onCreate(context: Context) {
    attachToLifecycle(ToolbarHelper)
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: ExpeditionBinding) {
    ToolbarHelper.init(viewBinding!!.menu, navigator)
    navigator.showNow(DogListStep(::goToDetailsScreen))
  }

  private fun goToDetailsScreen(name: String) {
    navigator.show(DogDetailsScreen(name))
  }
}
