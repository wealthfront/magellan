package com.wealthfront.magellan.sample

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magellan.compose.ComposeJourney
import com.example.magellan.compose.ComposeStepWrappingViewStep
import com.example.magellan.compose.Displayable
import com.wealthfront.magellan.sample.App.Provider.appComponent
import javax.inject.Inject

class FirstJourney(
  private val goToSecondJourney: () -> Unit
) : ComposeJourney() {

  @Inject lateinit var toaster: Toaster

  @Composable
  override fun Content() {
    Column {
      Box(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { goToSecondJourney() }) {
          Text("Start next journey")
        }
      }
      Displayable(navigator)
    }
  }

  override fun onCreate(context: Context) {
    appComponent.inject(this)
    navigator.goTo(IntroStep(::goToLearnMore))
  }

  private fun goToLearnMore() {
    navigator.goTo(ComposeStepWrappingViewStep(LearnMoreStep()))
  }
}
