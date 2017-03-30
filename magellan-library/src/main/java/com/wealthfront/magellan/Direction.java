package com.wealthfront.magellan;

/**
 * Represent the direction in whcih we are navigation. Either forward or backward.
 */
public enum Direction {

  FORWARD(1),
  BACKWARD(-1);

  private final int sign;

  Direction(int sign) {
    this.sign = sign;
  }

  public int sign() {
    return sign;
  }

}
