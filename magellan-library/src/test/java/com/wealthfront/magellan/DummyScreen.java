package com.wealthfront.magellan;

import android.content.Context;

class DummyScreen extends LegacyScreen<BaseScreenView<DummyScreen>> {

  private BaseScreenView<DummyScreen> view;

  DummyScreen(BaseScreenView<DummyScreen> view) {
    this.view = view;
  }

  @Override
  protected BaseScreenView<DummyScreen> createView(Context context) {
    return view;
  }

}