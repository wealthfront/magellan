package com.ryanmoelter.magellanx.compose

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.coroutines.CreatedLifecycleScope
import com.ryanmoelter.magellanx.core.coroutines.ShownLifecycleScope
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.attachFieldToLifecycle
import kotlinx.coroutines.CoroutineScope

public abstract class ComposeStep : ComposeSection(), Navigable<@Composable () -> Unit>

public abstract class ComposeSection :
  LifecycleAwareComponent(), Displayable<@Composable () -> Unit> {

  override val view: (@Composable () -> Unit)?
    get() = {
      WhenShown {
        Content()
      }
    }

  public var createdScope: CoroutineScope by attachFieldToLifecycle(CreatedLifecycleScope()) { it }
    @VisibleForTesting set

  public var shownScope: CoroutineScope by attachFieldToLifecycle(ShownLifecycleScope()) { it }
    @VisibleForTesting set

  @Composable
  protected abstract fun Content()
}
