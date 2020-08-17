package com.wealthfront.magellan.navigation;

import com.wealthfront.magellan.view.ActionBarConfig;

import org.jetbrains.annotations.NotNull;

public interface ActionBarConfigListener {

  void onNavigate(@NotNull ActionBarConfig actionBarConfig);

}
