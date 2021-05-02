package com.example.magellan.compose

import androidx.compose.runtime.Composable
import com.wealthfront.magellan.core.Displayable

@Composable
public fun Displayable(displayable: Displayable<@Composable () -> Unit>) {
  displayable.view!!()
}
