package com.wealthfront.magellan.sample.advanced;

import android.content.Context;

import com.wealthfront.magellan.BaseScreenView;

class ActionBarHiddenView extends BaseScreenView<ActionBarHiddenScreen> {

  public ActionBarHiddenView(Context context) {
    super(context);
    inflate(R.layout.action_bar_hidden);
  }

}
