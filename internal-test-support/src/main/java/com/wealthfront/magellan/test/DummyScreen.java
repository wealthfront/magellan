package com.wealthfront.magellan.test;

import android.content.Context;

import com.wealthfront.magellan.BaseScreenView;
import com.wealthfront.magellan.Screen;

import org.jetbrains.annotations.NotNull;

public class DummyScreen extends Screen<BaseScreenView<DummyScreen>> {

  private BaseScreenView<DummyScreen> view;

  public DummyScreen(BaseScreenView<DummyScreen> view) {
    this.view = view;
  }

  @Override
  protected BaseScreenView<DummyScreen> createView(@NotNull Context context) {
    return view;
  }

}