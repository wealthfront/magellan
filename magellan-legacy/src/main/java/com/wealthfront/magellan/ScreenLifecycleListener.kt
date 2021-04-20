package com.wealthfront.magellan

import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigableListener

public interface ScreenLifecycleListener : NavigableListener {

  public fun onShow(navigable: NavigableCompat) {}

  public fun onHide(navigable: NavigableCompat) {}

  @JvmDefault
  override fun onNavigatedTo(navigable: NavigableCompat) {
    onShow(navigable)
  }

  @JvmDefault
  override fun onNavigatedFrom(navigable: NavigableCompat) {
    onHide(navigable)
  }
}
