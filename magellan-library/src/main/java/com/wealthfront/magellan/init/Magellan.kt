package com.wealthfront.magellan.init

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX
import com.wealthfront.magellan.init.Magellan.customDefaultTransition
import com.wealthfront.magellan.init.Magellan.disableAnimations
import com.wealthfront.magellan.init.Magellan.logDebugInfo
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.MagellanTransition

public object Magellan {

  internal var logDebugInfo: Boolean = false
  internal var disableAnimations: Boolean = false
  internal var customDefaultTransition: MagellanTransition = DefaultTransition()
  public var navigationOverrides: Set<NavigationOverride> = emptySet()

  @JvmStatic
  @JvmOverloads
  public fun init(
    disableAnimations: Boolean = false,
    logDebugInfo: Boolean = false,
    defaultTransition: MagellanTransition = DefaultTransition(),
    navigationOverrides: Set<NavigationOverride> = emptySet()
  ) {
    this.logDebugInfo = logDebugInfo
    this.disableAnimations = disableAnimations
    this.customDefaultTransition = defaultTransition
    this.navigationOverrides = navigationOverrides
  }

  public fun registerNavigationOverrides(overrides: Set<NavigationOverride>) {
    this.navigationOverrides = overrides
  }
}

public data class NavigationOverride(
  val conditions: (
    navigationDelegate: NavigationDelegate,
    navigable: NavigableCompat
  ) -> Boolean,
  val navigationOperation: (navigationDelegate: NavigationDelegate) -> Unit
)

internal fun shouldRunAnimations(): Boolean = !disableAnimations

internal fun shouldLogDebugInfo(): Boolean = logDebugInfo

@RestrictTo(LIBRARY_GROUP_PREFIX)
public fun getDefaultTransition(): MagellanTransition = customDefaultTransition
