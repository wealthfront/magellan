package com.wealthfront.magellan.init

import com.wealthfront.magellan.init.Magellan.disableAnimations
import com.wealthfront.magellan.init.Magellan.logDebugInfo

public object Magellan {

  internal var logDebugInfo: Boolean = false
  internal var disableAnimations: Boolean = false

  public fun init(logDebugInfo: Boolean = false, disableAnimations: Boolean = false) {
    this.logDebugInfo = logDebugInfo
    this.disableAnimations = disableAnimations
  }
}

internal fun shouldRunAnimations(): Boolean = !disableAnimations

internal fun shouldLogDebugInfo(): Boolean = logDebugInfo
