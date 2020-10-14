package com.wealthfront.magellan

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.getContainingUClass

internal val AVOID_USING_ACTIVITY = Issue.create(
  id = AvoidUsingActivity::class.simpleName!!,
  briefDescription = "Use the context provided in the lifecycle objects instead.",
  explanation = "Activity is available here only for use to set the title of the navigable.",
  category = Category.CORRECTNESS,
  priority = PRIORITY_MED,
  severity = Severity.WARNING,
  implementation = Implementation(AvoidUsingActivity::class.java, Scope.JAVA_FILE_SCOPE)
)

private const val ACTIVITY = "android.app.Activity"
private const val NAVIGABLE_COMPAT = "com.wealthfront.magellan.navigation.NavigableCompat"

internal class AvoidUsingActivity : Detector(), Detector.UastScanner {

  override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

  override fun createUastHandler(context: JavaContext) = ActivityAccessChecker(context)

  class ActivityAccessChecker(private val context: JavaContext) : UElementHandler() {

    override fun visitCallExpression(node: UCallExpression) {
      if (node.isSubtypeOfNavigableCompat() && node.receiverType?.canonicalText == ACTIVITY) {
        context.report(
          AVOID_USING_ACTIVITY,
          node,
          context.getLocation(node),
          "Avoid using the activity instance present in the superclass. " +
            "Instead use the context provided in the lifecycle methods."
        )
      }
    }
  }
}

private fun UCallExpression.isSubtypeOfNavigableCompat(): Boolean {
  return getContainingUClass()?.superTypes?.any { it.canonicalText == NAVIGABLE_COMPAT } ?: false
}
