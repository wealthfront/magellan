package com.wealthfront.magellan.sample

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.magellan.compose.ComposeStep

internal class IntroStep(
  private val goToLearnMore: () -> Unit
) : ComposeStep() {

  @Composable
  override fun Compose() = IntroStepContent(goToLearnMore)
}

@Composable
internal fun IntroStepContent(goToLearnMore: () -> Unit) {
  Button(onClick = goToLearnMore) {
    Text("Go to learn more")
  }
}

@Preview(device = Devices.PIXEL_3, name = "Intro step")
@Composable
internal fun IntroStepPreview() {
  IntroStepContent { }
}
