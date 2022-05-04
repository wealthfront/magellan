package com.ryanmoelter.magellanx.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import kotlinx.coroutines.flow.map

@Composable
public fun Displayable<@Composable () -> Unit>.Content(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    view!!()
  }
}

@Composable
public fun Displayable(
  displayable: Displayable<@Composable () -> Unit>,
  modifier: Modifier = Modifier
): Unit = displayable.Content(modifier)

@Composable
public fun LifecycleOwner.WhenShown(Content: @Composable () -> Unit) {
  val isShownFlow = remember {
    currentStateFlow
      .map { lifecycleState ->
        when (lifecycleState) {
          is Destroyed, is Created -> false
          is Shown, is Resumed -> true
        }
      }
  }
  val isShown by isShownFlow.collectAsState(false)
  if (isShown) {
    Content()
  }
}
