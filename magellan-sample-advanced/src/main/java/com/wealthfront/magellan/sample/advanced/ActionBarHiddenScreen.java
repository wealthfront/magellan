package com.wealthfront.magellan.sample.advanced;

import android.content.Context;

import com.wealthfront.magellan.Screen;


class ActionBarHiddenScreen extends Screen<ActionBarHiddenView> {

  @Override
  protected ActionBarHiddenView createView(Context context) {
    return new ActionBarHiddenView(context);
  }

  @Override
  protected boolean shouldAnimateActionBar() {
    return true;
  }

  @Override
  protected boolean shouldShowActionBar() {
    return false;
  }

  @Override
  protected int getActionBarColorRes() {
    return R.color.colorPrimary;
  }
}
