package com.wealthfront.magellan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
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
  @CallSuper
  protected void onShow(Context context) {
    for (Screen screen : screens) {
      screen.recreateView(getActivity(), getNavigator());
      screen.createDialog();
      screen.onShow(context);
    }
  }

  @Override
  @CallSuper
  protected void onRestore(Bundle savedInstanceState) {
    for (Screen screen : screens) {
      screen.onRestore(savedInstanceState);
    }
  }

  @Override
  @CallSuper
  protected void onResume(Context context) {
    for (Screen screen : screens) {
      screen.onResume(context);
    }
  }

  @Override
  @CallSuper
  protected void onPause(Context context) {
    for (Screen screen : screens) {
      screen.onPause(context);
    }
  }

  @Override
  @CallSuper
  protected void onSave(Bundle outState) {
    for (Screen screen : screens) {
      screen.onSave(outState);
    }
  }

  @Override
  @CallSuper
  protected void onHide(Context context) {
    for (Screen screen : screens) {
      screen.onHide(context);
      screen.destroyDialog();
      screen.destroyView();
    }
  }

  protected final List<S> getScreens() {
    return new ArrayList<>(screens);
  }

}
