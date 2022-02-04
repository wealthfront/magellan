package com.wealthfront.magellan.core

import android.view.View

public interface Displayable {

  public val view: View?

  public var templatedView: View?

  public fun transitionStarted() {}

  public fun transitionFinished() {}
}
