package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.init.shouldLogDebugInfo
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import java.util.ArrayDeque
import java.util.Deque

public class NavigationTraverser(private val root: NavigableCompat) {

  public fun getGlobalBackStack(): NavigationNode {
    return constructTree(root)
  }

  private fun constructTree(navigable: NavigableCompat): NavigationNode {
    val navNode = NavigationNode(navigable, mutableListOf())
    if (navNode.hasBackstack) {
      val backStack = navNode.getBackstack()
      while (backStack.isNotEmpty()) {
        val node = backStack.removeLast()
        val childNavNode = constructTree(node.navigable)
        navNode.addChild(childNavNode)
      }
    }
    return navNode
  }

  public fun getGlobalBackstackDescription(): String {
    val rootNode = getGlobalBackStack()
    val sb = StringBuilder()
    getPrintableGlobalBackstack(rootNode, 0, sb)
    return sb.toString()
  }

  public fun logGlobalBackStack() {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, getGlobalBackstackDescription())
    }
  }

  private fun getPrintableGlobalBackstack(navNode: NavigationNode, depth: Int, sb: StringBuilder) {
    sb.appendLine()
    for (i in 0 until depth) {
      sb.append("\t")
    }
    sb.append(navNode.value.javaClass.simpleName)
    navNode.children.forEach { childNode ->
      getPrintableGlobalBackstack(childNode, depth + 1, sb)
    }
  }
}

public data class NavigationNode(
  val value: NavigableCompat,
  var children: List<NavigationNode>
) {

  val hasBackstack: Boolean
    get() = (value as? LifecycleOwner)?.children?.mapNotNull { it as? Navigator }?.isNotEmpty() ?: false

  public fun getBackstack(): Deque<NavigationEvent> {
    val backStackDeepCopy = ArrayDeque<NavigationEvent>()
    (value as LifecycleOwner).children.mapNotNull { it as? Navigator }.first().backStack.toList().toCollection(backStackDeepCopy)
    return backStackDeepCopy
  }

  public fun addChild(child: NavigationNode) {
    children = children + child
  }
}
