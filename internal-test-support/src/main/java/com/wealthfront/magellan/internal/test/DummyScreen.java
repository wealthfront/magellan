package com.wealthfront.magellan.internal.test;

import android.content.Context;

import com.wealthfront.magellan.BaseScreenView;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.navigation.NavigableCompat;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

public class DummyScreen extends Screen<BaseScreenView<DummyScreen>> {

  private BaseScreenView<DummyScreen> view;

  public DummyScreen(BaseScreenView<DummyScreen> view) {
    this.view = view;
  }

  @Override
  protected BaseScreenView<DummyScreen> createView(@NotNull Context context) {
    return view;
  }

  @NonNull
  @Override
  public NavigableCompat getCurrentNavigable() {
    return this;
  }
}
