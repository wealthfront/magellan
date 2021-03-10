package com.wealthfront.magellan

import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.NO_ANIM
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition

/**
 * Represents the different types of navigation we support:
 *
 *  * SHOW: represent a modal type of Navigation, where the Screen is only shown if not already displayed, and
 * with a default animation of sliding the new screen from the bottom.
 *  * GO: is the normal type of navigation, with a default animation of sliding both screens in sync from the right
 * to the left.
 *  * NO_ANIM: represent a normal navigation but with no animation.
 *
 */
public enum class NavigationType {

  SHOW, GO, NO_ANIM
}

internal fun NavigationType.toTransition(): MagellanTransition {
  return when (this) {
    GO -> getDefaultTransition()
    SHOW -> ShowTransition()
    NO_ANIM -> NoAnimationTransition()
  }
}
