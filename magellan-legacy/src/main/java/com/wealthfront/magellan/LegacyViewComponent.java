package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.VisibleForTesting;

public class LegacyViewComponent<V extends ViewGroup & ScreenView> implements LifecycleAware {

  private final Screen<V> screen;
  private V view;
  private Activity activity;
  private SparseArray<Parcelable> viewState;

  public LegacyViewComponent(Screen<V> screen) {
    this.screen = screen;
  }

  public final V getView() {
    return view;
  }

  public final Activity getActivity() {
    return activity;
  }

  @VisibleForTesting
  public final void setView(V view) {
    this.view = view;
  }

  @VisibleForTesting
  public final void setActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void create(@NotNull Context context) {
    this.activity = (Activity) context;
  }

  @Override
  public void show(@NotNull Context context) {
    view = screen.createView(activity);
    // noinspection unchecked
    view.setScreen(screen);
    if (viewState != null) {
      view.restoreHierarchyState(viewState);
    }
  }

  @Override
  public void resume(@NotNull Context context) { }

  @Override
  public void pause(@NotNull Context context) { }

  @Override
  public void hide(@NotNull Context context) {
    if (view != null) {
      viewState = new SparseArray<>();
      view.saveHierarchyState(viewState);
    }
    view = null;
  }

  @Override
  public void destroy(@NotNull Context context) {
    activity = null;
  }

  @Override
  public boolean backPressed() {
    return false;
  }
}
