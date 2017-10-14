package com.wealthfront.magellan.kotlin

internal class Preconditions private constructor() {

  init {
    throw AssertionError()
  }

  companion object {

    fun <T> checkNotNull(reference: T?): T {
      if (reference == null) {
        throw NullPointerException()
      }
      return reference
    }

    fun <T> checkNotNull(reference: T?, message: String): T {
      if (reference == null) {
        throw NullPointerException(message)
      }
      return reference
    }

    fun checkArgument(expression: Boolean) {
      if (!expression) {
        throw IllegalArgumentException()
      }
    }

    fun checkArgument(expression: Boolean, message: String) {
      if (!expression) {
        throw IllegalArgumentException(message)
      }
    }

    fun checkState(expression: Boolean) {
      if (!expression) {
        throw IllegalStateException()
      }
    }

    fun checkState(expression: Boolean, message: String) {
      if (!expression) {
        throw IllegalStateException(message)
      }
    }
  }

}
