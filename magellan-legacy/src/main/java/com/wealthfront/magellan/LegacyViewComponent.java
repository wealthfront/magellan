package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleAware;

import org.jetbrains.annotations.NotNull;

public class LegacyViewComponent<V extends ViewGroup & ScreenView> implements LifecycleAware {

  private final Screen<V> screen;
  private SparseArray<Parcelable> viewState;

  public LegacyViewComponent(@NotNull Screen<V> screen) {
    this.screen = screen;
  }

  @Override
  public void show(@NotNull Context context) {
    setActivity(context);
    V view = screen.createView(context);
    // noinspection unchecked
    view.setScreen(screen);
    if (viewState != null) {
      view.restoreHierarchyState(viewState);
    }
    screen.setView(view);
  }

  @Override
  public void hide(@NotNull Context context) {
    V view = screen.getView();
    if (view != null) {
      viewState = new SparseArray<>();
      view.saveHierarchyState(viewState);
    }
    screen.setView(null);
    screen.setActivity(null);
  }

  private void setActivity(@NotNull Context context) {
    screen.setActivity(findActivity(context));
  }

  @NotNull
  private Activity findActivity(@NotNull Context context) {
    if (context instanceof Activity) {
      return (Activity) context;
    } else if (context instanceof ContextWrapper) {
      return findActivity(((ContextWrapper) context).getBaseContext());
    } else {
      throw new IllegalStateException("Context must be Activity or wrap Activity");
    }
  }
}
