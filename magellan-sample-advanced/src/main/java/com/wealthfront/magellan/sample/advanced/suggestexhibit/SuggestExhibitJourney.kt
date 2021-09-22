package com.wealthfront.magellan.sample.advanced.suggestexhibit

import android.content.Context
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.SuggestExhibitBinding
import com.wealthfront.magellan.transitions.DefaultTransition

class SuggestExhibitJourney(private val completeSuggestion: () -> Unit) :
  Journey<SuggestExhibitBinding>(
    SuggestExhibitBinding::inflate,
    SuggestExhibitBinding::suggestExhibitContainer
  ) {

  override fun onCreate(context: Context) {
    navigator.goTo(SuggestDetailStep(this::goToConfirmation))
  }

  override fun onShow(context: Context, binding: SuggestExhibitBinding) {
    ToolbarHelperProvider.toolbarHelper.setTitle("Suggest Exhibit")
    ToolbarHelperProvider.toolbarHelper.showToolbar()
  }

  private fun goToConfirmation() {
    navigator.navigate(Direction.FORWARD) { backstack ->
      backstack.clear()
      val next = NavigationEvent(SuggestConfirmationStep(completeSuggestion), DefaultTransition())
      backstack.push(next)
      next
    }
  }
}
