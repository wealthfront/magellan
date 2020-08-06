package com.wealthfront.magellan

import com.wealthfront.magellan.navigation.LinearNavigator

open class LegacyNavigator internal constructor(
  container: () -> ScreenContainer
) : LinearNavigator(container) {

  override fun maybeAttachNavigator() {
    if (currentNavigable is Screen<*>) {
      (currentNavigable as Screen<*>).setNavigator(this)
    }
  }
}
