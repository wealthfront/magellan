package com.wealthfront.magellan.navigation

import android.content.Context
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForMocking
public class CurrentNavigableProvider @Inject constructor() : NavigationListener, LifecycleAwareComponent() {

  public var navigable: NavigableCompat? = null
    protected set

  public fun isCurrentNavigable(other: NavigableCompat): Boolean = navigable == other

  override fun onNavigatedTo(navigable: NavigableCompat) {
    this.navigable = navigable
  }

  override fun onCreate(context: Context) {
    NavigationPropagator.addNavigableListener(this)
  }

  override fun onDestroy(context: Context) {
    NavigationPropagator.removeNavigableListener(this)
  }
}
