package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.LifecycleOwner

class NavigationTraverser(private val root: Step<*>) {

  fun getGlobalBackStack(): NavigationNode {
    return constructTree(root)
  }

  private fun constructTree(navigationItem: NavigationItem): NavigationNode {
    val navNode = NavigationNode(navigationItem, mutableListOf())
    if (navNode.hasBackstack) {
      navNode.backStack.forEach { navItem ->
        val childNavNode = constructTree(navItem)
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
  val value: NavigationItem,
  var children: List<NavigationNode>
) {

  val hasBackstack: Boolean
    get() = (value as? LifecycleOwner)?.children?.mapNotNull { it as? Navigator }?.isNotEmpty()
      ?: false

  val backStack: List<NavigationItem>
    get() = (value as LifecycleOwner).children.mapNotNull { it as? Navigator }.first().backStack

  fun addChild(child: NavigationNode) {
    children = children + child
  }
}
