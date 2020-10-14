package com.wealthfront.magellan.view;

import android.view.Menu;
import org.jetbrains.annotations.NotNull;
import androidx.annotation.ColorRes;

public interface ActionBarModifier {

  int DEFAULT_ACTION_BAR_COLOR_RES = 0;

  /**
   * Override this method to dynamically change the menu.
   */
  default void onUpdateMenu(@NotNull Menu menu) {}

  /**
   * @return true if we should show the ActionBar, false otherwise (true by default).
   */
  default boolean shouldShowActionBar() {
    return true;
  }

  /**
   * @return true if we should animate the ActionBar, false otherwise (true by default).
   */
  default boolean shouldAnimateActionBar() {
    return true;
  }

  /**
   * @return true if we need to pass the onUpdateMenu calls to the navigables children, false otherwise (false by default).
   */
  default boolean shouldShowChildNavigablesMenu() {
    return false;
  }

  /**
   * @return the color of the ActionBar (invalid by default).
   */
  @ColorRes
  default int getActionBarColorRes() {
    return DEFAULT_ACTION_BAR_COLOR_RES;
  }

}
