package com.ryanmoelter.magellanx.compose

import androidx.compose.runtime.Composable

public open class SimpleComposeStep(
  private val SimpleContent: @Composable SimpleComposeStep.() -> Unit
) : ComposeStep() {

  @Composable
  override fun Content() {
    SimpleContent()
  }
}
