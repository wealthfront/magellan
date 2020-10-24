package com.wealthfront.magellan.lifecycle;

import android.content.Context;

public interface LifecycleAware {
  
  default void create(Context context) {}

  default void show(Context context) {}

  default void resume(Context context) {}

  default void pause(Context context) {}

  default void hide(Context context) {}

  default void destroy(Context context) {}

  default boolean backPressed() {
    return false;
  }
}
