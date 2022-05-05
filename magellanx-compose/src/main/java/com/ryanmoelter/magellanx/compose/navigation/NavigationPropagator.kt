package com.ryanmoelter.magellanx.compose.navigation

import com.ryanmoelter.magellanx.core.Navigable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Suppress("ObjectPropertyName")
public object NavigationPropagator {
  internal val _beforeNavigation: MutableSharedFlow<Unit> = MutableSharedFlow()
  public val beforeNavigation: SharedFlow<Unit>
    get() = _beforeNavigation

  internal val _afterNavigation: MutableSharedFlow<Unit> = MutableSharedFlow()
  public val afterNavigation: SharedFlow<Unit>
    get() = _afterNavigation

  internal val _onNavigatedTo: MutableSharedFlow<Navigable<*>> = MutableSharedFlow()
  public val onNavigatedTo: SharedFlow<Navigable<*>>
    get() = _onNavigatedTo

  internal val _onNavigatedFrom: MutableSharedFlow<Navigable<*>> = MutableSharedFlow()
  public val onNavigatedFrom: SharedFlow<Navigable<*>>
    get() = _onNavigatedFrom
}
