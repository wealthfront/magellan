package com.ryanmoelter.magellanx.core.init

import com.ryanmoelter.magellanx.core.init.Magellan.disableAnimations
import com.ryanmoelter.magellanx.core.init.Magellan.logDebugInfo

public object Magellan {

  internal var logDebugInfo: Boolean = false
  internal var disableAnimations: Boolean = false

  @JvmStatic
  @JvmOverloads
  public fun init(
    disableAnimations: Boolean = false,
    logDebugInfo: Boolean = false
  ) {
    this.logDebugInfo = logDebugInfo
    this.disableAnimations = disableAnimations
  }
}

internal fun shouldRunAnimations(): Boolean = !disableAnimations

internal fun shouldLogDebugInfo(): Boolean = logDebugInfo
