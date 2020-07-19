package com.wealthfront.magellan.sample

import android.content.Context
import android.util.Log
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.navigation.NavigationPropagator.addNavigableListener
import com.wealthfront.magellan.navigation.NavigationPropagator.removeNavigableListener
import com.wealthfront.magellan.navigation.NavigationTraverser
import com.wealthfront.magellan.navigation.NavigableListener
import com.wealthfront.magellan.sample.databinding.ExpeditionBinding

class Expedition : Journey<ExpeditionBinding>(
  ExpeditionBinding::inflate,
  ExpeditionBinding::container
), NavigableListener {

  private val navigationTraverser = NavigationTraverser(this)

  override fun onCreate(context: Context) {
    addNavigableListener(this)
    navigator.goTo(FirstJourney(::goToSecondJourney))
  }

  override fun onDestroy(context: Context) {
    removeNavigableListener(this)
  }

  private fun goToSecondJourney() {
    navigator.show(SecondJourney())
  }

  override fun onNavigableShown(navigable: Navigable) {
    Log.i(this::class.java.simpleName, "Shown: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigableHidden(navigable: Navigable) {
    Log.i(this::class.java.simpleName, "Hidden: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigate() {
    navigationTraverser.logGlobalBackStack()
  }
}
