package com.wealthfront.magellan

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope.Companion.RESOURCE_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element

internal val INVALID_CHILD_IN_SCREEN_CONTAINER = Issue.create(
  id = InvalidChildInScreenContainer::class.simpleName!!,
  briefDescription = "ScreenContainer should not have child declared in XML.",
  explanation = "ScreenContainers are used to inflate the view's associated with steps/journey's internally." +
    "If you declare child views in the XML, it may result in unexpected results.",
  category = Category.CORRECTNESS,
  priority = PRIORITY_HIGH,
  severity = ERROR,
  implementation = Implementation(InvalidChildInScreenContainer::class.java, RESOURCE_FILE_SCOPE)
)

const val SCREEN_CONTAINER = "com.wealthfront.magellan.ScreenContainer"

class InvalidChildInScreenContainer : LayoutDetector() {

  override fun getApplicableElements() = listOf(SCREEN_CONTAINER)

  override fun visitElement(context: XmlContext, element: Element) {
    if (element.hasChildNodes()) {
      context.report(
        INVALID_CHILD_IN_SCREEN_CONTAINER,
        element,
        context.getElementLocation(element),
        "Remove child views inside the ScreenContainer."
      )
    }
  }
}
