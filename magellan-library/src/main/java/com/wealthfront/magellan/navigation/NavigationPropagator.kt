package com.wealthfront.magellan.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking

public object NavigationPropagator {

  private val _events = MutableSharedFlow<NavigationLifecycleEvent>()
  public val events: SharedFlow<NavigationLifecycleEvent> = _events.asSharedFlow()

  public fun emit(event: NavigationLifecycleEvent) {
    runBlocking { _events.emit(event) }
  }
}

public sealed class NavigationLifecycleEvent {
  public data class NavigatedTo(val navigable: NavigableCompat) : NavigationLifecycleEvent()
  public data class NavigatedFrom(val navigable: NavigableCompat) : NavigationLifecycleEvent()
  public object BeforeNavigation : NavigationLifecycleEvent()
  public object AfterNavigation : NavigationLifecycleEvent()
}
