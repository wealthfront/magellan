package com.wealthfront.magellan.core

import java.util.LinkedList

fun Navigable.getScreenTraversal(): List<Navigable> {
  val listOfScreens = LinkedList<Navigable>()
  var currentNavigable: Navigable? = this
  while (currentNavigable != null) {
    listOfScreens.addLast(currentNavigable)
    currentNavigable = currentNavigable.nextNavigable
  }
  return listOfScreens
}

fun Navigable.getTraversalDescription(): String {
  val listOfScreens = getScreenTraversal()
  return "Backstack : ${listOfScreens.joinToString(separator = " -> ") { it.javaClass.simpleName }}"
}