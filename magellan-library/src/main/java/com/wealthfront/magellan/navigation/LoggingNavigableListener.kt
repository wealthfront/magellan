package com.wealthfront.magellan.navigation

import android.content.Context
import android.util.Log
import com.wealthfront.magellan.coroutines.CreatedLifecycleScope
import com.wealthfront.magellan.init.shouldLogDebugInfo
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class LoggingNavigableListener @Inject constructor(
  private val navigationTraverser: NavigationTraverser,
  createdScope: CreatedLifecycleScope
) : LifecycleAwareComponent() {

  private val createdScope by lifecycle(createdScope)

  override fun onCreate(context: Context) {
    createdScope.launch {
      NavigationPropagator.events
        .collect { event ->
          when (event) {
            NavigationLifecycleEvent.AfterNavigation -> afterNavigation()
            is NavigationLifecycleEvent.NavigatedFrom -> onNavigatedFrom(event.navigable)
            is NavigationLifecycleEvent.NavigatedTo -> onNavigatedTo(event.navigable)
            NavigationLifecycleEvent.BeforeNavigation -> { }
          }
        }
    }
  }

  private fun onNavigatedTo(navigable: NavigableCompat) {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, "Navigated To: ${navigable.javaClass.simpleName}")
    }
  }

  private fun onNavigatedFrom(navigable: NavigableCompat) {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, "Navigated From: ${navigable.javaClass.simpleName}")
    }
  }

  private fun afterNavigation() {
    navigationTraverser.logGlobalBackStack()
  }
}
