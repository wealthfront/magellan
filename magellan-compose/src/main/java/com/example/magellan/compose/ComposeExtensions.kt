package com.example.magellan.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import kotlinx.coroutines.flow.map

@Composable
public fun Displayable(
  displayable: Displayable<@Composable () -> Unit>,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    displayable.view!!()
  }
}

@Composable
public fun Displayable<@Composable () -> Unit>.Content(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    view!!()
  }
}

@Composable
public fun LifecycleOwner.WhenShown(Content: @Composable () -> Unit) {
  val isShown by currentStateFlow
    .map { lifecycleState ->
      when (lifecycleState) {
        is Destroyed, is Created -> false
        is Shown, is Resumed -> true
      }
    }
    .collectAsState(false)
  if (isShown) {
    Content()
  }
}
