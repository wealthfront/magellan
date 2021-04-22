package com.wealthfront.magellan.lifecycle;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

public interface LifecycleAware {
  
  default void create(@NotNull Context context) {}

  default void show(@NotNull Context context) {}

  default void resume(@NotNull Context context) {}

  default void pause(@NotNull Context context) {}

  default void hide(@NotNull Context context) {}

  default void destroy(@NotNull Context context) {}

  default boolean backPressed() {
    return false;
  }
}
