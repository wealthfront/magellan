package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.lifecycle.LifecycleOwner

class NavigationTraverser(private val root: NavigableCompat) {

  fun getGlobalBackStack(): NavigationNode {
    return constructTree(root)
  }

  private fun constructTree(navigable: NavigableCompat): NavigationNode {
    val navNode = NavigationNode(navigable, mutableListOf())
    if (navNode.hasBackstack) {
      navNode.backStack.forEach { navEvent ->
        val childNavNode = constructTree(navEvent.navigable)
        navNode.addChild(childNavNode)
      }
    }
    return navNode
  }

  fun printGlobalBackstack(): String {
    val rootNode = getGlobalBackStack()
    val sb = StringBuilder()
    getPrintableGlobalBackstack(rootNode, 0, sb)
    return sb.toString()
  }

  fun logGlobalBackStack() {
    Log.i(this::class.java.simpleName, printGlobalBackstack())
  }

  private fun getPrintableGlobalBackstack(navNode: NavigationNode, depth: Int, sb: StringBuilder) {
    sb.appendln()
    for (i in 0 until depth) {
      sb.append("\t")
    }
    sb.append(navNode.value.javaClass.simpleName)
    navNode.children.forEach { childNode ->
      getPrintableGlobalBackstack(childNode, depth + 1, sb)
    }
  }
}

data class NavigationNode(
  val value: NavigableCompat,
  var children: List<NavigationNode>
) {

  val hasBackstack: Boolean
    get() = (value as? LifecycleOwner)?.children?.mapNotNull { it as? Navigator }?.isNotEmpty()
      ?: false

  val backStack: List<NavigationEvent>
    get() = (value as LifecycleOwner).children.mapNotNull { it as? Navigator }.first().backStack

  fun addChild(child: NavigationNode) {
    children = children + child
  }
}
