package com.ryanmoelter.magellanx.core.navigation

import com.ryanmoelter.magellanx.core.Displayable

public interface LinearNavigator<ViewType : Any, EventType : Any> : Displayable<ViewType> {
  public val backStack: List<EventType>
}
