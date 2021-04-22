package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.coroutines.CreatedLifecycleScope
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigationLifecycleEvent.NavigatedTo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForMocking
public class CurrentNavigableProvider @Inject constructor(
  createdScope: CreatedLifecycleScope
) : LifecycleAwareComponent() {

  private val createdScope by lifecycle(createdScope)
  public var navigable: NavigableCompat? = null
    protected set

  public fun isCurrentNavigable(other: NavigableCompat): Boolean = navigable == other

  override fun onCreate(context: Context) {
    createdScope.launch {
      NavigationPropagator.events
        .filterIsInstance<NavigatedTo>()
        .collect { event ->
          navigable = event.navigable
        }
    }
  }
}
