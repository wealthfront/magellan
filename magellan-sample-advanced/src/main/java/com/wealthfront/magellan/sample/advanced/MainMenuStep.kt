package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.MainMenuBinding
import javax.inject.Inject

class MainMenuStep(
  private val goToCollection: () -> Unit,
  private val goToDesignCereal: () -> Unit,
  private val goToOrderTickets: () -> Unit,
  private val goToRequestExhibit: () -> Unit
) : Step<MainMenuBinding>(MainMenuBinding::inflate) {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: MainMenuBinding) {
    binding.browseCollection.setOnClickListener { goToCollection() }
    binding.designCereal.setOnClickListener { goToDesignCereal() }
    binding.orderTickets.setOnClickListener { goToOrderTickets() }
    binding.suggestExhibit.setOnClickListener { goToRequestExhibit() }
    toolbarHelper.hideToolbar()
  }
}
