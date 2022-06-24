package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.LazySetNavigator
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.cerealcollection.BrowseCollectionJourney
import com.wealthfront.magellan.sample.advanced.databinding.MainMenuBinding
import com.wealthfront.magellan.sample.advanced.designcereal.DesignCerealStartStep
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsStartStep
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitStartStep
import javax.inject.Inject

class MainMenuStep(
  goToDesignCereal: () -> Unit,
  goToOrderTickets: () -> Unit,
  goToSuggestExhibit: () -> Unit
) : Step<MainMenuBinding>(MainMenuBinding::inflate) {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  private val browseCollectionJourney = BrowseCollectionJourney()
  private val suggestExhibitStart = SuggestExhibitStartStep(goToSuggestExhibit)
  private val orderTicketsStart = OrderTicketsStartStep(goToOrderTickets)
  private val designCerealStart = DesignCerealStartStep(goToDesignCereal)

  private var selectedTab = R.id.browseCollection
  private var navigator: LazySetNavigator by attachFieldToLifecycle(
    LazySetNavigator { viewBinding!!.navigableContainer }
  )

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.addNavigables(
      setOf(
        browseCollectionJourney,
        designCerealStart,
        orderTicketsStart,
        suggestExhibitStart
      )
    )
  }

  override fun onShow(context: Context, binding: MainMenuBinding) {
    binding.bottomBarNavigation.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.browseCollection -> {
          selectedTab = R.id.browseCollection
          showBrowseCollection()
          true
        }
        R.id.designCereal -> {
          selectedTab = R.id.designCereal
          showDesignCereal()
          true
        }
        R.id.orderTickets -> {
          selectedTab = R.id.orderTickets
          showOrderTickets()
          true
        }
        R.id.suggestExhibit -> {
          selectedTab = R.id.suggestExhibit
          showRequestExhibit()
          true
        }
        else -> {
          throw IllegalArgumentException("Invalid menu selection!")
        }
      }
    }
    binding.bottomBarNavigation.selectedItemId = selectedTab
  }

  private fun showBrowseCollection() {
    navigator.replace(browseCollectionJourney)
  }

  private fun showDesignCereal() {
    navigator.replace(designCerealStart)
  }

  private fun showOrderTickets() {
    navigator.replace(orderTicketsStart)
  }

  private fun showRequestExhibit() {
    navigator.replace(suggestExhibitStart)
  }
}
