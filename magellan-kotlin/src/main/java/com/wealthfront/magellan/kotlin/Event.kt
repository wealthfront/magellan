package com.wealthfront.magellan.kotlin

internal class Event(private val navigationType: NavigationType, private val direction: Direction, private val backStackDescription: String) {

  override fun toString(): String =
      navigationType.toString() + " " + direction + " - Backstack: " + backStackDescription

}
