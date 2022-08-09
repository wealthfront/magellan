package com.wealthfront.magellan.sample.migration

import android.content.Context
import com.wealthfront.magellan.LegacyExpedition
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.lifecycle.attachLateinitFieldToLifecycle
import com.wealthfront.magellan.navigation.CurrentNavigableProvider
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.sample.migration.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.migration.databinding.ExpeditionBinding
import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreen
import com.wealthfront.magellan.sample.migration.tide.DogListStep
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Expedition @Inject constructor() : LegacyExpedition<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
) {

  @set:Inject var navListener: LoggingNavigableListener by attachLateinitFieldToLifecycle()
  @Inject lateinit var currentNavigableProvider: CurrentNavigableProvider
  private val lifecycleMetricsListener by attachFieldToLifecycle(LifecycleMetricsListener())

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    attachToLifecycle(ToolbarHelper)
    setCurrentNavProvider(currentNavigableProvider)
  }

  override fun onShow(context: Context, binding: ExpeditionBinding) {
    ToolbarHelper.init(viewBinding!!.menu, navigator)
    navigator.showNow(DogListStep(::goToDetailsScreen))
  }

  override fun onDestroy(context: Context) {
    removeFromLifecycle(ToolbarHelper)
  }

  private fun goToDetailsScreen(name: String) {
    navigator.show(DogDetailsScreen(name))
  }
}
