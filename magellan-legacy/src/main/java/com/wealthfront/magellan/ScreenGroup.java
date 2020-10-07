package com.wealthfront.magellan;

import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleState;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A Screen containing a list of screens. Useful to display reusable Screens that can be either in another one or on
 * it's own.
 */
public abstract class ScreenGroup<S extends Screen, V extends ViewGroup & ScreenView> extends Screen<V> {

  private List<S> screens;

  public ScreenGroup() {
    this.screens = new ArrayList<>();
  }

  public ScreenGroup(@NotNull List<S> screens) {
    this.screens = new ArrayList<>(screens);
  }

  public void addScreen(@NotNull S screen) {
    attachToLifecycle(screen, LifecycleState.Destroyed.INSTANCE);
    screens.add(screen);
  }

  public void addScreens(@NotNull List<S> screens) {
    for (S screen : screens) {
      addScreen(screen);
    }
  }

  protected final List<S> getScreens() {
    return new ArrayList<>(screens);
  }

}
