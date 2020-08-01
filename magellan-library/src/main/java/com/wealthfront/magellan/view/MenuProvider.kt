package com.wealthfront.magellan.view

import android.content.Context
import android.view.Menu
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.core.childNavigables
import com.wealthfront.magellan.navigation.NavigableListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuProvider @Inject constructor() : NavigableListener {

  private var menu: Menu? = null
  private var currentJourney: Journey<*>? = null
  private var currentStep: Step<*>? = null

  fun setMenu(menu: Menu?) {
    this.menu = menu
    updateMenu()
  }

  override fun onNavigableShown(navigable: Navigable) {
    if (navigable is Journey<*>) {
      currentJourney = navigable
    } else if (navigable is Step<*>) {
      currentStep = navigable
    }
    updateMenu()
  }

  override fun onNavigableHidden(navigable: Navigable) {
    if (navigable == currentJourney) {
      currentJourney = null
      currentStep = null
    }
  }

  override fun destroy(context: Context) {
    menu = null
    currentJourney = null
    currentStep = null
  }

  private fun updateMenu() {
    if (menu != null) {
      hideAllMenuItems()
      callUpdateMenu()
    }
  }

  private fun hideAllMenuItems() {
    for (i in 0 until menu!!.size()) {
      menu!!.getItem(i).isVisible = false
    }
  }

  private fun callUpdateMenu() {
    currentJourney?.onUpdateMenu(menu!!)
    currentJourney?.childNavigables()?.forEach { it.onUpdateMenu(menu!!) }
    currentStep?.onUpdateMenu(menu!!)
    currentStep?.childNavigables()?.forEach { it.onUpdateMenu(menu!!) }
  }
}
