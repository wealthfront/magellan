package com.wealthfront.magellan;

import android.view.View;
import android.content.Context;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;

/**
 * Base class to easily implement a {@link ScreenView}. Inherit from {@link FrameLayout}.
 */
public class BaseScreenView<S extends Screen> extends FrameLayout implements ScreenView<S> {

  private S screen;

  public BaseScreenView(@NotNull Context context) {
    super(context);
  }

  @Override
  public final void setScreen(@NotNull S screen) {
    this.screen = screen;
  }

  @Override
  @NotNull
  public final S getScreen() {
    return screen;
  }

  @NotNull
  public View inflate(int resource) {
    return inflate(getContext(), resource, this);
  }
}
