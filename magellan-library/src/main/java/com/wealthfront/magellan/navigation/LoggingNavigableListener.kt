package com.wealthfront.magellan.navigation

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingNavigableListener @Inject constructor(private val navigationTraverser: NavigationTraverser) : NavigableListener {

  override fun onNavigableShown(navigationItem: NavigationItem) {
    Log.i(this::class.java.simpleName, "Shown: ${navigationItem.javaClass.simpleName}")
  }

  override fun onNavigableHidden(navigationItem: NavigationItem) {
    Log.i(this::class.java.simpleName, "Hidden: ${navigationItem.javaClass.simpleName}")
  }

  override fun onNavigate() {
    navigationTraverser.logGlobalBackStack()
  }
}
