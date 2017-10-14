package com.wealthfront.magellan.kotlin

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
enum class NavigationType {
  SHOW,
  GO,
  NO_ANIM
}
