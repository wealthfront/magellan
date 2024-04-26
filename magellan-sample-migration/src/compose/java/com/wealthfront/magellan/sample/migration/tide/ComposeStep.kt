package com.wealthfront.magellan.sample.migration.tide

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.createAndAttachFieldToLifecycleWhenShown
import java.util.UUID

abstract class ComposeStep : Navigable, LifecycleAwareComponent() {

  private var state: SaveableStateHolder? = null

  final override var view: ComposeView? by createAndAttachFieldToLifecycleWhenShown { ComposeView(it) }
    @VisibleForTesting set

  fun setContent(content: @Composable () -> Unit) {
    view?.setContent {
      if (state == null) {
        state = rememberSaveableStateHolder()
      }
      Box(modifier = Modifier) {
        state!!.SaveableStateProvider(UUID.randomUUID()) {
          content()
        }
      }
    }
  }
}
