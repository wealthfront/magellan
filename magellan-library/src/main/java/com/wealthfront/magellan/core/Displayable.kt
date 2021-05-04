package com.wealthfront.magellan.core

public interface Displayable<ViewType : Any> {

  public val view: ViewType?

  public fun transitionStarted() {}

  public fun transitionFinished() {}
}
