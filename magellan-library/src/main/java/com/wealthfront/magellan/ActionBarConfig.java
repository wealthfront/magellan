package com.wealthfront.magellan;

import android.app.ActionBar;
import android.support.annotation.ColorRes;
import android.widget.Toolbar;

/**
 * Used to configure the {@link ActionBar} or {@link Toolbar} in {@link NavigationListener#onNavigate(ActionBarConfig)}
 * with what the current Screen asked for.
 */
public final class ActionBarConfig {

  private final boolean visible;
  private final boolean animated;
  @ColorRes private final int colorRes;

  private ActionBarConfig(Builder builder) {
    this.colorRes = builder.colorRes;
    this.visible = builder.visible;
    this.animated = builder.animated;
  }

  @ColorRes
  public int colorRes() {
    return colorRes;
  }

  public boolean animated() {
    return animated;
  }

  public boolean visible() {
    return visible;
  }

  public static Builder with() {
    return new Builder();
  }

  public static final class Builder {

    private boolean visible;
    private boolean animated;
    @ColorRes private int colorRes;

    private Builder() {}

    public Builder visible(boolean visible) {
      this.visible = visible;
      return this;
    }

    public Builder animated(boolean animated) {
      this.animated = animated;
      return this;
    }

    public Builder colorRes(@ColorRes int colorRes) {
      this.colorRes = colorRes;
      return this;
    }

    public ActionBarConfig build() {
      return new ActionBarConfig(this);
    }

  }

}
