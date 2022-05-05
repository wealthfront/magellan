package com.ryanmoelter.magellanx

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.ERROR
import org.jetbrains.uast.UField
import org.jetbrains.uast.util.isConstructorCall

internal val ENFORCE_LIFECYCLE_AWARE_ATTACHMENT = Issue.create(
  id = EnforceLifecycleAwareAttachment::class.simpleName!!,
  briefDescription = "Lifecycle aware objects should be instantiated inside a lifecycle delegate. Eg. val someObject by lifecycle(SomeObject())",
  explanation = "All lifecycle aware objects need to be attached to a parent for listening to lifecycle.",
  category = Category.CORRECTNESS,
  priority = PRIORITY_HIGH,
  severity = ERROR,
  implementation = Implementation(EnforceLifecycleAwareAttachment::class.java, JAVA_FILE_SCOPE)
)

internal class EnforceLifecycleAwareAttachment : Detector(), Detector.UastScanner {

  override fun getApplicableUastTypes() = listOf(UField::class.java)

  override fun createUastHandler(context: JavaContext) = LifecycleAwareChecker(context)

  class LifecycleAwareChecker(private val context: JavaContext) : UElementHandler() {

    override fun visitField(node: UField) {
      if (context.isKotlin() && node.isLifecycleAware() && node.isConstructor()) {
        context.report(
          ENFORCE_LIFECYCLE_AWARE_ATTACHMENT,
          node,
          context.getLocation(node),
          "In order to make this lifecycle aware work as expected, " +
            "please attach it to the lifecycle owner with a lifecycle delegate. " +
            "Eg. `val someObject by lifecycle(SomeObject())` or `lateinit var someObject: SomeObject by lateinitLifecycle()`"
        )
      }
    }
  }
}

private fun JavaContext.isKotlin() = file.name.endsWith("kt")

private fun UField.isLifecycleAware(): Boolean {
  return typeReference?.type?.superTypes?.any {
    it.canonicalText == "com.ryanmoelter.magellanx.lifecycle.LifecycleAware"
  } ?: false
}

private fun UField.isConstructor(): Boolean {
  return uastInitializer?.isConstructorCall() ?: false
}
