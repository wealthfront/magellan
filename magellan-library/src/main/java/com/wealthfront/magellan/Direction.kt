package com.wealthfront.magellan

/**
 * Represent the direction in which we are navigating, either forward or backward.
 */
enum class Direction(private val sign: Int) {

  FORWARD(1), BACKWARD(-1);

  fun sign(): Int {
    return sign
  }

  internal fun indexToAddView(container: ScreenContainer): Int {
    return if (this == FORWARD) {
      container.childCount
    } else {
      0
    }
  }
}
