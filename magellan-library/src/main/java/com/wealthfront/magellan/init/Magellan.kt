package com.wealthfront.magellan.init

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.init.Magellan.customDefaultTransition
import com.wealthfront.magellan.init.Magellan.disableAnimations
import com.wealthfront.magellan.init.Magellan.logDebugInfo
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import kotlinx.coroutines.CoroutineScope

public object Magellan {

  internal var logDebugInfo: Boolean = false
  internal var disableAnimations: Boolean = false
  internal var customDefaultTransition: MagellanTransition = DefaultTransition()
  public var shownScopeProvider: () -> LifecycleAware = { ShownLifecycleScope() }

  @JvmStatic
  @JvmOverloads
  public fun init(
    disableAnimations: Boolean = false,
    logDebugInfo: Boolean = false,
    defaultTransition: MagellanTransition = DefaultTransition(),
  ) {
    this.logDebugInfo = logDebugInfo
    this.disableAnimations = disableAnimations
    this.customDefaultTransition = defaultTransition
  }
}

internal fun shouldRunAnimations(): Boolean = !disableAnimations

internal fun shouldLogDebugInfo(): Boolean = logDebugInfo

@RestrictTo(LIBRARY_GROUP_PREFIX)
public fun getDefaultTransition(): MagellanTransition = customDefaultTransition
