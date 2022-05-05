package com.ryanmoelter.magellanx.compose

import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigator
import com.ryanmoelter.magellanx.core.lifecycle.attachFieldToLifecycle

public abstract class ComposeJourney : ComposeStep() {

  protected var navigator: ComposeNavigator by attachFieldToLifecycle(ComposeNavigator())

  @Composable
  protected override fun Content(): Unit = navigator.Content()
}
