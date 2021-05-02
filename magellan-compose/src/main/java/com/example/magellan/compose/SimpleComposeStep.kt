package com.example.magellan.compose

import androidx.compose.runtime.Composable

public class SimpleComposeStep(public val Content: @Composable SimpleComposeStep.() -> Unit) : ComposeStep() {

  @Composable
  override fun Compose() {
    this.Content()
  }
}