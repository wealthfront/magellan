package com.wealthfront.magellan;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.wealthfront.magellan.lifecycle.LifecycleState.*;

/**
 * A Screen containing a list of screens. Useful to display reusable Screens that can be either in another one or on
 * it's own.
 */
public abstract class ScreenGroup<S extends Screen, V extends ViewGroup & ScreenView> extends Screen<V> implements MultiScreen<S> {

  private List<S> screens;

  public ScreenGroup() {
    this(new ArrayList<>());
  }

  public ScreenGroup(@NotNull List<S> screens) {
    this.screens = new ArrayList<>(screens);
    addScreens(screens);
  }

  @Override
  public void addScreen(@NotNull S screen) {
    screens.add(screen);
    attachToLifecycleWithNavigator(screen);
  }

  @NotNull
  public final List<S> getScreens() {
    return new ArrayList<>(screens);
  }

  private void attachToLifecycleWithNavigator(@NotNull S screen) {
    attachToLifecycle(screen, Destroyed.INSTANCE);
    attachToLifecycle(new LifecycleAware() {
      @Override
      public void create(@NotNull Context context) {
        screen.setNavigator(getNavigator());
      }
    }, Destroyed.INSTANCE);
  }
}