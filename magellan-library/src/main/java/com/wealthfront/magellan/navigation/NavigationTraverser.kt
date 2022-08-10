package com.wealthfront.magellan.navigation

import android.util.Log
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.debug.CONNECTOR_L
import com.wealthfront.magellan.debug.INDENT_SPACE
import com.wealthfront.magellan.debug.VERTICAL_LINE
import com.wealthfront.magellan.debug.VERTICAL_T
import com.wealthfront.magellan.init.shouldLogDebugInfo

@OpenForMocking
public class NavigationTraverser(private val root: NavigableCompat) {

  public fun getGlobalBackStack(): NavigationNode {
    return root.createSnapshot()
  }

  public fun getGlobalBackstackDescription(): String {
    val rootNode = getGlobalBackStack()
    return rootNode.getNavigationSnapshot()
  }

  private fun NavigationNode.getNavigationSnapshot(): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append(describeSelf(""))
    children.mapIndexed { index, node ->
      node.getNavigationSnapshotRecursive("", index == children.lastIndex)
    }
      .forEach { stringBuilder.append(it) }
    return stringBuilder.toString()
  }

  private fun NavigationNode.getNavigationSnapshotRecursive(indent: String, isLastChild: Boolean): String {
    val stringBuilder = StringBuilder()
    val lineChar = if (isLastChild) CONNECTOR_L else VERTICAL_T
    stringBuilder.append(describeSelf(indent + lineChar + INDENT_SPACE))
    children.mapIndexed { index, node ->
      val childIndent = indent + if (isLastChild) {
        " $INDENT_SPACE"
      } else {
        "$VERTICAL_LINE$INDENT_SPACE"
      }
      node.getNavigationSnapshotRecursive(
        indent + childIndent,
        index == children.lastIndex
      )
    }.forEach { stringBuilder.append(it) }
    return stringBuilder.toString()
  }

  public fun logGlobalBackStack() {
    if (shouldLogDebugInfo()) {
      Log.d(this::class.java.simpleName, getGlobalBackstackDescription())
    }
  }
}

public interface NavigationNode {
  public val value: NavigableCompat
  public val children: List<NavigationNode>
}

public class BackstackNode(
  override val value: NavigableCompat,
  navigator: Navigator
) : NavigationNode {
  override val children: List<NavigationNode> = navigator.backStack.reversed().map { it.navigable.createSnapshot() }
}

public class LeafNode(
  override val value: NavigableCompat
) : NavigationNode {
  override val children: List<NavigationNode> = emptyList()
}

private fun NavigationNode.describeSelf(indent: String): String = "$indent${value.javaClass.simpleName}\n"
