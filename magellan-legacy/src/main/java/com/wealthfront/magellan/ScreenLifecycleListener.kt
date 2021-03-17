package com.wealthfront.magellan

import android.content.Context
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigableListener
import com.wealthfront.magellan.navigation.NavigationPropagator.addNavigableListener
import com.wealthfront.magellan.navigation.NavigationPropagator.removeNavigableListener

public interface ScreenLifecycleListener : NavigableListener {

  public fun onShow(navigable: NavigableCompat) {}

  public fun onHide(navigable: NavigableCompat) {}

  @JvmDefault
  override fun onNavigableShown(navigable: NavigableCompat) {
    onShow(navigable)
  }

  @JvmDefault
  override fun onNavigableHidden(navigable: NavigableCompat) {
    onHide(navigable)
  }

  @JvmDefault
  override fun show(context: Context) {
    addNavigableListener(this)
  }

  @JvmDefault
  override fun hide(context: Context) {
    removeNavigableListener(this)
  }
}