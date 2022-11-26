package com.ryanmoelter.magellanx

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

internal const val PRIORITY_MED = 5
internal const val PRIORITY_HIGH = 8
internal const val PRIORITY_MAX = 10

class LintRegistry : IssueRegistry() {

  override val issues: List<Issue>
    get() = listOf(
      ENFORCE_LIFECYCLE_AWARE_ATTACHMENT
    )

  override val api: Int
    get() = CURRENT_API
}
