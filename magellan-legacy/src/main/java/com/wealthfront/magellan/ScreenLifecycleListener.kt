package com.wealthfront.magellan

import android.content.Context
import com.wealthfront.magellan.coroutines.CreatedLifecycleScope
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationLifecycleEvent.AfterNavigation
import com.wealthfront.magellan.navigation.NavigationLifecycleEvent.BeforeNavigation
import com.wealthfront.magellan.navigation.NavigationLifecycleEvent.NavigatedFrom
import com.wealthfront.magellan.navigation.NavigationLifecycleEvent.NavigatedTo
import com.wealthfront.magellan.navigation.NavigationPropagator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

public interface ScreenLifecycleListener {

  public fun onShow(navigable: NavigableCompat) {}

  public fun onHide(navigable: NavigableCompat) {}
}

public class ScreenLifecycleListenerAdapter(
  internal val lifecycleListener: ScreenLifecycleListener
) : LifecycleAwareComponent() {

  private val createdScope by lifecycle(CreatedLifecycleScope())

  override fun onCreate(context: Context) {
    createdScope.launch {
      NavigationPropagator.events
        .collect { event ->
          when (event) {
            is NavigatedFrom -> lifecycleListener.onHide(event.navigable)
            is NavigatedTo -> lifecycleListener.onShow(event.navigable)
            AfterNavigation, BeforeNavigation -> { }
          }
        }
    }
  }
}
