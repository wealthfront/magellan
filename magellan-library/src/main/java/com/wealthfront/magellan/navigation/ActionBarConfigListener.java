package com.wealthfront.magellan.navigation;

import com.wealthfront.magellan.view.ActionBarConfig;

import org.jetbrains.annotations.Nullable;

public interface ActionBarConfigListener {

  void onNavigate(@Nullable ActionBarConfig actionBarConfig);

}
