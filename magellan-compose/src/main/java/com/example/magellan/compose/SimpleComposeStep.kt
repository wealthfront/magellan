package com.example.magellan.compose

import androidx.compose.runtime.Composable

public class SimpleComposeStep(
  private val SimpleContent: @Composable SimpleComposeStep.() -> Unit
) : ComposeStep() {

  @Composable
  override fun Content() {
    SimpleContent()
  }
}