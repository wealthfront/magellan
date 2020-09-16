package com.wealthfront.magellan;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MultiScreen<S extends Screen> {

  void addScreen(@NotNull S screen);

  void addScreens(@NotNull List<S> screens);

  List<S> getScreens();
}
