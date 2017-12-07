package com.wealthfront.magellan.support;

public class Preconditions {

  private Preconditions() {
    throw new AssertionError("Instance not possible");
  }

  static void checkNotNull(Object object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
  }
}
