package com.wealthfront.magellan;

final class Preconditions {

  private Preconditions() {
    throw new AssertionError();
  }

  static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  static <T> T checkNotNull(T reference, String message) {
    if (reference == null) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  static void checkArgument(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

  static void checkState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  static void checkState(boolean expression, String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

}
