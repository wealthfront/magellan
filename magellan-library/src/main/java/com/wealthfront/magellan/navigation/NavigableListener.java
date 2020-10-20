package com.wealthfront.magellan.navigation;

import android.content.Context;
import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

public interface NavigableListener extends LifecycleAware {

  default void onNavigableShown(@NotNull NavigableCompat navigable) {}

  default void onNavigableHidden(@NotNull NavigableCompat navigable) {}

  default void onNavigate() {}

  @Override
  default void show(@NotNull Context context) {
    NavigationPropagator.addNavigableListener(this);
  }

  @Override
  default void hide(@NotNull Context context) {
    NavigationPropagator.removeNavigableListener(this);
  }
}
