package com.wealthfront.magellan;

import org.jetbrains.annotations.NotNull;

public interface NavigationListener {

  void onNavigate(@NotNull ActionBarConfig actionBarConfig);

}
