package com.wealthfront.magellan.kotlin

/**
 * Represent the direction in which we are navigating, either forward or backward.
 */
enum class Direction(private val sign: Int) {

  FORWARD(1),
  BACKWARD(-1);

  fun sign(): Int = sign

}
