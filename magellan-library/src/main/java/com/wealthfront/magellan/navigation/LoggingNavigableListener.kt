package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.init.shouldLogDebugInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class LoggingNavigableListener @Inject constructor(private val navigationTraverser: NavigationTraverser) : NavigableListener {

  override fun onNavigatedTo(navigable: NavigableCompat) {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, "Navigated To: ${navigable.javaClass.simpleName}")
    }
  }

  override fun onNavigatedFrom(navigable: NavigableCompat) {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, "Navigated From: ${navigable.javaClass.simpleName}")
    }
  }

  override fun afterNavigation() {
    navigationTraverser.logGlobalBackStack()
  }
}
