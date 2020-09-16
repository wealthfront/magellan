package com.wealthfront.magellan;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MultiScreen<S extends Screen> {

  void addScreen(@NotNull S screen);

  default void addScreens(@NotNull List<S> screens) {
    for (S screen: screens) {
      addScreen(screen);
    }
  };

  List<S> getScreens();
}
