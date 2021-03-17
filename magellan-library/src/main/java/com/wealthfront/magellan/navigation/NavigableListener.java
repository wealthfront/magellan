package com.wealthfront.magellan.navigation;

import android.content.Context;

import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.CallSuper;

public interface NavigableListener extends LifecycleAware {

  default void onNavigableShown(@NotNull NavigableCompat navigable) {}

  default void onNavigableHidden(@NotNull NavigableCompat navigable) {}

  default void beforeNavigation() {}

  default void afterNavigation() {}

  @Override
  @CallSuper
  default void create(@NotNull Context context) {
    NavigationPropagator.addNavigableListener(this);
  }

  @Override
  @CallSuper
  default void destroy(@NotNull Context context) {
    NavigationPropagator.removeNavigableListener(this);
  }
}
