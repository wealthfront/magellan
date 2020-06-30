package com.wealthfront.magellan;

import android.view.ViewGroup;

/**
 * @deprecated use {@link Screen} instead.
 */
@Deprecated
public abstract class LegacyScreen<V extends ViewGroup & ScreenView> extends Screen<V> {

  private Navigator navigator;

  /**
   * @return the Navigator associated with this Screen.
   */
  public final Navigator getNavigator() {
    return navigator;
  }

  public final void setNavigator(Navigator navigator) {
    this.navigator = navigator;
  }

}
