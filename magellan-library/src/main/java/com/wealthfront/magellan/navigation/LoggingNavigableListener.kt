package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.Expedition
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingNavigableListener @Inject constructor(private val expedition: Expedition) : NavigableListener {

  override fun onNavigableShown(navigable: NavigableCompat) {
    Log.i(this::class.java.simpleName, "Shown: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigableHidden(navigable: NavigableCompat) {
    Log.i(this::class.java.simpleName, "Hidden: ${navigable.javaClass.simpleName}")
  }

  override fun onNavigate() {
    expedition.logGlobalBackStack()
  }
}
