package com.example.magellan.compose

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.lifecycle.lifecycleWithContext

@Composable
public fun Displayable(displayable: Displayable<View>, modifier: Modifier = Modifier) {
  AndroidView(modifier = modifier, factory = {
    if (displayable.view == null) {
      throw IllegalStateException(
        "View does not exist on ${displayable::class.java.simpleName}. " +
          "Is it attached to the lifecycle?"
      )
    }
    displayable.view!!
  })
}

public class ComposeStepWrapper(
  composeStep: Navigable<@Composable () -> Unit>
) : LifecycleAwareComponent(), Navigable<View> {

  public val composeStep: Navigable<@Composable () -> Unit> by lifecycle(composeStep)

  override val view: View? by lifecycleWithContext { context ->
    ComposeView(context).also { composeView ->
      composeView.setContent @Composable { Displayable(this.composeStep) }
    }
  }
}

public class ViewStepComposer(
  viewStep: Navigable<View>
) : LifecycleAwareComponent(), Navigable<@Composable () -> Unit> {

  public val viewStep: Navigable<View> by lifecycle(viewStep)

  override val view: @Composable () -> Unit = {
    Displayable(this.viewStep)
  }
}
