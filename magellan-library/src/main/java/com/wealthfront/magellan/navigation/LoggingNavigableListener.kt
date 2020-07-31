package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.core.Navigable

class LoggingNavigableListener(private val navigationTraverser: NavigationTraverser) : NavigableListener {

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