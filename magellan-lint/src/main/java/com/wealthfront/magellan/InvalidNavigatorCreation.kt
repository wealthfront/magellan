package com.wealthfront.magellan

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UField
import org.jetbrains.uast.kotlin.AbstractKotlinUVariable
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression

internal val INVALID_NAVIGATOR_CREATION = Issue.create(
  id = InvalidNavigatorCreation::class.simpleName!!,
  briefDescription = "Always use the navigator provided within the journey instead of creating a new one.",
  explanation = "The navigator inside each journey is used to provide context about the journey's backstack." +
    "And each journey should only contain one backstack therefore each journey should only have one navigator.",
  category = CORRECTNESS,
  priority = PRIORITY_MED,
  severity = WARNING,
  implementation = Implementation(InvalidNavigatorCreation::class.java, Scope.JAVA_FILE_SCOPE)
)

const val NAVIGATOR = "com.wealthfront.magellan.navigation.Navigator"
const val JOURNEY = "com.wealthfront.magellan.core.Journey"

internal class InvalidNavigatorCreation : Detector(), Detector.UastScanner {

  override fun getApplicableUastTypes() = listOf(UClass::class.java)

  override fun createUastHandler(context: JavaContext) = NavigatorChecker(context)

  class NavigatorChecker(private val context: JavaContext) : UElementHandler() {
    override fun visitClass(node: UClass) {
      if (!node.isSubClass(JOURNEY)) {
        return
      }

      node.fields
        .filter { it.type.isSubClass(NAVIGATOR) || it.isDelegate(NAVIGATOR) }
        .forEach { navigator ->
          val fix = LintFix.create()
            .replace()
            .range(context.getLocation(navigator))
            .name("Remove navigator instance")
            .all()
            .with("")
            .autoFix()
            .build()
          context.report(
            INVALID_NAVIGATOR_CREATION,
            navigator,
            context.getLocation(navigator),
            "Use the navigator provided by the Journey instead of creating your own instance.",
            fix
          )
        }
    }
  }
}

private fun UField.isDelegate(clazz: String): Boolean {
  val delegateExp = (this as? AbstractKotlinUVariable)?.delegateExpression as? KotlinUFunctionCallExpression
  val delegateType = (delegateExp?.returnType as? PsiClassReferenceType)?.reference
  val navType = delegateType?.parameterList?.typeArguments?.first()?.superTypes?.first()
  val isNavigator = navType?.superTypes?.any {
    it.canonicalText.contains(clazz)
  }
  return isNavigator ?: false
}

internal fun PsiType.isSubClass(clazz: String): Boolean {
  return superTypes.mapNotNull { it as? PsiClassReferenceType }.any { psiType ->
    psiType.reference.qualifiedName.contains(clazz)
  }
}

internal fun UClass.isSubClass(clazz: String): Boolean {
  return uastSuperTypes.any { type ->
    type.getQualifiedName()?.contains(clazz) ?: false
  }
}
