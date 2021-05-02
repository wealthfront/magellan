package com.wealthfront.magellan.core

import android.view.View

public interface Displayable<ViewType : Any> {

  public val view: ViewType?

  public fun transitionStarted() {}

  public fun transitionFinished() {}
}
