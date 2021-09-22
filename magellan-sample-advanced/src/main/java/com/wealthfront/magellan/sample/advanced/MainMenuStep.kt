package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.databinding.MainMenuBinding

class MainMenuStep(
  private val goToCollection: () -> Unit,
  private val goToDesignCereal: () -> Unit,
  private val goToOrderTickets: () -> Unit,
  private val goToRequestExhibit: () -> Unit
) : Step<MainMenuBinding>(MainMenuBinding::inflate) {

  override fun onShow(context: Context, binding: MainMenuBinding) {
    binding.browseCollection.setOnClickListener { goToCollection() }
    binding.designCereal.setOnClickListener { goToDesignCereal() }
    binding.orderTickets.setOnClickListener { goToOrderTickets() }
    binding.suggestExhibit.setOnClickListener { goToRequestExhibit() }
    ToolbarHelperProvider.toolbarHelper.hideToolbar()
  }
}
