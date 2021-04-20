package com.wealthfront.magellan.debug

import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleOwner

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
        index == children.lastIndex
      )
    }
    .forEach { stringBuilder.append(it) }

  return stringBuilder.toString()
}

private fun LifecycleAware.getTreeDescriptionInternal(
  indent: String,
  isLastChild: Boolean
): String {
  val stringBuilder = StringBuilder()
  val lineChar = if (isLastChild) CONNECTOR_L else VERTICAL_T
  stringBuilder.append(describeSelf(indent + lineChar + INDENT_SPACE))
  if (this is LifecycleOwner) {
    children
      .mapIndexed { index, lifecycleAware ->
        val childIndent = indent + if (isLastChild) {
          " $INDENT_SPACE"
        } else {
          "$VERTICAL_LINE$INDENT_SPACE"
        }
        lifecycleAware.getTreeDescriptionInternal(
          indent + childIndent,
          index == children.lastIndex
        )
      }
      .forEach { stringBuilder.append(it) }
  }

  return stringBuilder.toString()
}

private fun LifecycleOwner.describeSelf(indent: String): String = indent + this::class.java.simpleName + "\n"

private fun LifecycleAware.describeSelf(indent: String): String = indent + this::class.java.simpleName + "\n"

