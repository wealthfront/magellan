package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.databinding.MagellanSimpleJourneyBinding
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.transitions.DefaultTransition
import javax.inject.Inject

class SuggestExhibitJourney(private val completeSuggestion: () -> Unit) : SimpleJourney() {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    navigator.goTo(SuggestDetailStep(this::goToConfirmation))
  }

  override fun onShow(context: Context, binding: MagellanSimpleJourneyBinding) {
    toolbarHelper.setTitle("Suggest Exhibit")
    toolbarHelper.showToolbar()
  }

  private fun goToConfirmation() {
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.clear()
      val next = NavigationEvent(SuggestConfirmationStep(false, completeSuggestion), DefaultTransition())
      backstack.push(next)
      next.magellanTransition
    }
  }
}
