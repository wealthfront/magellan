package com.wealthfront.magellan;

public class LazyScreen<S extends Screen> {

  private boolean loaded;
  private S screen;

  LazyScreen(S screen) {
    this.screen = screen;
  }

  public boolean isLoaded() {
    return loaded;
  }

  S getScreen() {
    return screen;
  }

  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }
}