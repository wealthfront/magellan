package com.wealthfront.magellan.navigation

import android.content.Context
import android.util.Log
import com.wealthfront.magellan.init.shouldLogDebugInfo
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class LoggingNavigableListener @Inject constructor(
  private val navigationTraverser: NavigationTraverser
) : NavigationListener, LifecycleAwareComponent() {

  override fun onCreate(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun onDestroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }

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
