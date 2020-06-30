package com.wealthfront.magellan;

/**
 * The interface to be implemented by a View representing a Screen.
 * @see BaseScreenView
 */
public interface ScreenView<S extends Screen> {

  void setScreen(S screen);

  S getScreen();

}
