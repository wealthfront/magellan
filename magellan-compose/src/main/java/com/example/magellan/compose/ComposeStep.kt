package com.example.magellan.compose

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import kotlinx.coroutines.CoroutineScope

public abstract class ComposeStep : LifecycleAwareComponent(), Navigable<@Composable () -> Unit> {

  override val view: (@Composable () -> Unit)?
    get() = {
      WhenShown {
        Content()
      }
    }

  public var shownScope: CoroutineScope by lifecycle(ShownLifecycleScope()) { it }
    @VisibleForTesting set

  @Composable
  protected abstract fun Content()
}