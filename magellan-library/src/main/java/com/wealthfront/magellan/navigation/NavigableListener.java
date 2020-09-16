package com.wealthfront.magellan.navigation;

import android.content.Context;
import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

public interface NavigableListener extends LifecycleAware {

  default void onNavigableShown(NavigableCompat navigable) {}

  default void onNavigableHidden(NavigableCompat navigable) {}

  default void onNavigate() {}

  @Override
  default void create(@NotNull Context context) {
    NavigationPropagator.addNavigableListener(this);
  }

  @Override
  default void destroy(@NotNull Context context) {
    NavigationPropagator.removeNavigableListener(this);
  }
}
