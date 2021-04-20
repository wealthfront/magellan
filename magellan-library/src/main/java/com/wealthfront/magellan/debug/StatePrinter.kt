package com.wealthfront.magellan.debug

import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import com.wealthfront.magellan.lifecycle.LifecycleState

private const val VERTICAL_LINE = '|'
private const val VERTICAL_T = '├'
private const val CONNECTOR_L = '└'
private const val INDENT_SPACE = ' '

public fun LifecycleOwner.getTreeDescription(): String {
  val stringBuilder = StringBuilder()
  stringBuilder.append(describeSelf(""))
  children
    .mapIndexed { index, lifecycleAware ->
      lifecycleAware.getTreeDescriptionInternal(
        "",
        index == children.lastIndex,
        currentState
      )
    }
    .forEach { stringBuilder.append(it) }

  return stringBuilder.toString()
}

private fun LifecycleAware.getTreeDescriptionInternal(
  indent: String,
  isLastChild: Boolean,
  parentLifecycleState: LifecycleState
): String {
  val stringBuilder = StringBuilder()
  val lineChar = if (isLastChild) CONNECTOR_L else VERTICAL_T
  if (this is LifecycleOwner) {
    stringBuilder.append(describeSelf(indent + lineChar + INDENT_SPACE))
    children
      .mapIndexed { index, lifecycleAware ->
        val childIndent = indent + if (isLastChild) {
          " $INDENT_SPACE"
        } else {
          "$VERTICAL_LINE$INDENT_SPACE"
        }
        lifecycleAware.getTreeDescriptionInternal(
          indent + childIndent,
          index == children.lastIndex,
          currentState
        )
      }
      .forEach { stringBuilder.append(it) }
  } else {
    stringBuilder.append(describeSelf(indent + lineChar + INDENT_SPACE, parentLifecycleState))

  }

  return stringBuilder.toString()
}

private fun LifecycleOwner.describeSelf(indent: String): String =
  "$indent${this::class.java.simpleName} (${currentState::class.java.simpleName})\n"

private fun LifecycleAware.describeSelf(indent: String, parentLifecycleState: LifecycleState): String =
  "$indent${this::class.java.simpleName} (${parentLifecycleState::class.java.simpleName}?)\n"

