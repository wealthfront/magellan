package com.wealthfront.magellan;

/**
 * Represents the different types of navigation we support:
 * <ul>
 *   <li>SHOW: represent a modal type of Navigation, where the Screen is only shown if not already displayed, and
 *   with a default animation of sliding the new screen from the bottom.</li>
 *   <li>GO: is the normal type of navigation, with a default animation of sliding both screens in sync from the right
 *   to the left.</li>
 *   <li>NO_ANIM: represent a normal navigation but with no animation.</li>
 * </ul>
 */
public enum NavigationType {
  SHOW,
  GO,
  NO_ANIM
}
