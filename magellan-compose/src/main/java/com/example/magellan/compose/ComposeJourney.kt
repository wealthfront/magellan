package com.example.magellan.compose

import androidx.compose.runtime.Composable
import com.example.magellan.compose.navigation.ComposeNavigator
import com.wealthfront.magellan.lifecycle.lifecycle

public abstract class ComposeJourney : ComposeStep() {

  protected var navigator: ComposeNavigator by lifecycle(ComposeNavigator())

  @Composable
  protected override fun Content(): Unit = Displayable(navigator)
}