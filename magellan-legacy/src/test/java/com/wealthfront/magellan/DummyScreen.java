package com.wealthfront.magellan;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

class DummyScreen extends Screen<BaseScreenView<DummyScreen>> {

  private BaseScreenView<DummyScreen> view;

  DummyScreen(BaseScreenView<DummyScreen> view) {
    this.view = view;
  }

  @Override
  protected BaseScreenView<DummyScreen> createView(@NotNull Context context) {
    return view;
  }

}