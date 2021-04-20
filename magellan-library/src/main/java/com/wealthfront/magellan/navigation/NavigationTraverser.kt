package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.debug.CONNECTOR_L
import com.wealthfront.magellan.debug.INDENT_SPACE
import com.wealthfront.magellan.debug.VERTICAL_LINE
import com.wealthfront.magellan.debug.VERTICAL_T
import com.wealthfront.magellan.init.shouldLogDebugInfo
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import java.util.ArrayDeque
import java.util.Deque

@OpenForMocking
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
    return rootNode.getNavigationSnapshot()
  }

  private fun NavigationNode.getNavigationSnapshot(): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(describeSelf(""))
    children
      .mapIndexed { index, node ->
        node.getNavigationSnapshotRecursive("", index == children.lastIndex)
      }
      .forEach { stringBuilder.append(it) }

    return stringBuilder.toString()
  }

  private fun NavigationNode.getNavigationSnapshotRecursive(
    indent: String,
    isLastChild: Boolean
  ): String {
    val stringBuilder = StringBuilder()
    val lineChar = if (isLastChild) CONNECTOR_L else VERTICAL_T
    stringBuilder.append(describeSelf(indent + lineChar + INDENT_SPACE))
    children
      .mapIndexed { index, node ->
        val childIndent = indent + if (isLastChild) {
          " $INDENT_SPACE"
        } else {
          "$VERTICAL_LINE$INDENT_SPACE"
        }
        node.getNavigationSnapshotRecursive(
          indent + childIndent,
          index == children.lastIndex
        )
      }
      .forEach { stringBuilder.append(it) }
    return stringBuilder.toString()
  }

  public fun logGlobalBackStack() {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, getGlobalBackstackDescription())
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

private fun NavigationNode.describeSelf(indent: String): String = "$indent${this.value::class.java.simpleName}\n"