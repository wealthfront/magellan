package com.wealthfront.magellan;

/**
 * Use {@link Navigator#addLifecycleListener(ScreenLifecycleListener)} to add a lifecycle listener that will be called
 * whenever a Screen's onShow or onHide method is called.
 */
public interface ScreenLifecycleListener {

  void onShow(Screen screen);

  void onHide(Screen screen);

}
