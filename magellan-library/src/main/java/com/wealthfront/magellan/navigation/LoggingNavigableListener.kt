package com.wealthfront.magellan.navigation

import android.util.Log
import javax.inject.Inject

class LoggingNavigableListener @Inject constructor(private val navigationTraverser: NavigationTraverser) : NavigableListener {

  override fun onNavigableShown(navigable: NavigableCompat) {
    Log.i(this::class.java.simpleName, "Shown: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigableHidden(navigable: NavigableCompat) {
    Log.i(this::class.java.simpleName, "Hidden: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigate() {
    navigationTraverser.logGlobalBackStack()
  }
}
