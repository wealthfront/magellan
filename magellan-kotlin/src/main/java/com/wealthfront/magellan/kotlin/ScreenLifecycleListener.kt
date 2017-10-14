package com.wealthfront.magellan.kotlin

/**
 * Use [Navigator.addLifecycleListener] to add a lifecycle listener that will be called
 * whenever a Screen's onShow or onHide method is called.
 */
interface ScreenLifecycleListener {

  fun onShow(screen: Screen<*>)

  fun onHide(screen: Screen<*>)

}