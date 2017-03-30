package com.wealthfront.magellan;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Base class to easily implement a {@link ScreenView}. Inherit from {@link FrameLayout}.
 */
public class BaseScreenView<S extends Screen> extends FrameLayout implements ScreenView<S> {

  private S screen;

  public BaseScreenView(Context context) {
    super(context);
  }

  @Override
  public final void setScreen(S screen) {
    this.screen = screen;
  }

  @Override
  public final S getScreen() {
    return screen;
  }

}
