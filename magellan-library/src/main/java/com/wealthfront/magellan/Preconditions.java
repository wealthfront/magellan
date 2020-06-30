package com.wealthfront.magellan;

public final class Preconditions {

  private Preconditions() {
    throw new AssertionError();
  }

  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  public static <T> T checkNotNull(T reference, String message) {
    if (reference == null) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  public static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  public static void checkArgument(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void checkState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  public static void checkState(boolean expression, String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

}
