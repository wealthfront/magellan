package com.wealthfront.magellan;

import android.app.Activity;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.VisibleForTesting;

public abstract class Screen<V extends ViewGroup & ScreenView> extends com.wealthfront.magellan.flow.Screen<V> {

  private Activity activity;
  private V view;
  private Navigator navigator;
  private SparseArray<Parcelable> viewState;

  public final Navigator getNavigator() {
    return navigator;
  }

  final V recreateView(Activity activity, Navigator navigator) {
    this.activity = activity;
    this.navigator = navigator;
    view = createView(activity);
    // noinspection unchecked
    view.setScreen(this);
    if (viewState != null) {
      view.restoreHierarchyState(viewState);
    }
    return view;
  }

  @VisibleForTesting
  public final void setNavigator(Navigator navigator) {
    this.navigator = navigator;
  }

}
