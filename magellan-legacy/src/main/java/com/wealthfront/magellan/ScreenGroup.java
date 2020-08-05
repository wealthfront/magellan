package com.wealthfront.magellan;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

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

  public ScreenGroup(List<S> screens) {
    this.screens = new ArrayList<>(screens);
  }

  public void addScreen(S screen) {
    this.checkOnCreateNotYetCalled("Cannot add screen after onCreate is called");
    screen.checkOnCreateNotYetCalled("Cannot add a screen after onCreate is called on the screen");
    screens.add(screen);
  }

  public void addScreens(List<S> screens) {
    for (S screen : screens) {
      addScreen(screen);
    }
  }

  @Override
  protected void onShow(Context context) {
    super.onShow(context);
    for (Screen screen : screens) {
      screen.recreateView(context);
      screen.onShow(context);
    }
  }

  @Override
  protected void onRestore(Bundle savedInstanceState) {
    for (Screen screen : screens) {
      screen.onRestore(savedInstanceState);
    }
  }

  @Override
  protected void onResume(Context context) {
    for (Screen screen : screens) {
      screen.onResume(context);
    }
  }

  @Override
  protected void onPause(Context context) {
    for (Screen screen : screens) {
      screen.onPause(context);
    }
  }

  @Override
  protected void onHide(Context context) {
    super.onHide(context);
    for (Screen screen : screens) {
      screen.onHide(context);
      screen.destroyView();
    }
  }

  protected final List<S> getScreens() {
    return new ArrayList<>(screens);
  }

}
