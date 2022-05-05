package com.ryanmoelter.magellanx.core.debug

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState

internal const val VERTICAL_LINE = '|'
internal const val VERTICAL_T = '├'
internal const val CONNECTOR_L = '└'
internal const val INDENT_SPACE = ' '

public fun LifecycleOwner.getLifecycleStateSnapshot(): String {
  val stringBuilder = StringBuilder()
  stringBuilder.append(describeSelf(""))
  children
    .mapIndexed { index, lifecycleAware ->
      lifecycleAware.getLifecycleStateSnapshotRecursive(
        "",
        index == children.lastIndex,
        currentState
      )
    }
    .forEach { stringBuilder.append(it) }

  return stringBuilder.toString()
}

private fun LifecycleAware.getLifecycleStateSnapshotRecursive(
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
        lifecycleAware.getLifecycleStateSnapshotRecursive(
          childIndent,
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

private fun LifecycleAware.describeSelf(
  indent: String,
  parentLifecycleState: LifecycleState
): String =
  "$indent${this::class.java.simpleName} (${parentLifecycleState::class.java.simpleName}?)\n"
