package com.wealthfront.magellan;

import android.content.Context;
import android.view.Menu;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleAware;
import com.wealthfront.magellan.lifecycle.LifecycleState;
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.VisibleForTesting;

public abstract class LazyTabsScreenGroup<S extends Screen, V extends ViewGroup & ScreenView> extends Screen<V> {

  @VisibleForTesting List<LazyScreen<S>> lazyScreens;
  @VisibleForTesting LazyScreen<S> selectedLazyScreen;

  public LazyTabsScreenGroup() {
    this(new ArrayList<>());
  }

  public LazyTabsScreenGroup(List<S> screens) {
    this.lazyScreens = new ArrayList<>();
    addScreens(screens);
  }

  public void addScreen(S screen) {
    attachToLifecycleWithNavigator(screen);
    lazyScreens.add(new LazyScreen<>(screen));
    if (selectedLazyScreen == null) {
      selectedLazyScreen = lazyScreens.get(0);
    }
  }

  public void addScreens(List<S> screens) {
    for (S screen : screens) {
      addScreen(screen);
      attachToLifecycleWithNavigator(screen);
    }
  }

  private void attachToLifecycleWithNavigator(@NotNull S screen) {
    attachToLifecycle(screen, Destroyed.INSTANCE);
    attachToLifecycle(new LifecycleAware() {
      @Override
      public void create(@NotNull Context context) {
        screen.setNavigator(getNavigator());
      }
    }, Destroyed.INSTANCE);
  }

  private LazyScreen<S> findLazyScreen(S screen) {
    for (LazyScreen<S> lazyScreen : lazyScreens) {
      if (lazyScreen.getScreen() == screen) {
        return lazyScreen;
      }
    }
    throw new IllegalArgumentException("Screen " + screen.getClass().getSimpleName() + " doesn't exist");
  }

  public void setSelectedScreen(S screen) {
    LazyScreen<S> lazyScreen = findLazyScreen(screen);
    if (selectedLazyScreen == lazyScreen || getActivity() == null) {
      return;
    }
    selectedLazyScreen = lazyScreen;
    getActivity().invalidateOptionsMenu();
    showSelectedScreen();
    getActivity().setTitle(getTitle(getActivity()));
  }

  public S getSelectedScreen() {
    return selectedLazyScreen.getScreen();
  }

  @Override
  public void onUpdateMenu(@NotNull Menu menu) {
    selectedLazyScreen.getScreen().onUpdateMenu(menu);
  }

  @Override
  protected void onShow(@NotNull Context context) {
    for (LazyScreen<S> screen : lazyScreens) {
      screen.setLoaded(false);
    }
    showSelectedScreen();
  }

  @VisibleForTesting
  protected void showSelectedScreen() {
    if (!selectedLazyScreen.isLoaded()) {
      selectedLazyScreen.setLoaded(true);
      attachToLifecycle(selectedLazyScreen.getScreen(), Destroyed.INSTANCE);
    }
    onScreenDisplayed(selectedLazyScreen.getScreen());
  }

  public abstract void onScreenDisplayed(S screen);

  @NotNull
  @Override
  public String getTitle(@NotNull Context context) {
    return selectedLazyScreen.getScreen().getTitle(context);
  }

  @Override
  protected void onHide(@NotNull Context context) {
    for (LazyScreen<S> screen : lazyScreens) {
      if (screen.isLoaded()) {
        screen.setLoaded(false);
        removeFromLifecycle(screen.getScreen(), Destroyed.INSTANCE);
      }
    }
  }

  protected final List<S> getScreens() {
    List<S> screens = new ArrayList<>();

    for (LazyScreen<S> screen : lazyScreens) {
      screens.add(screen.getScreen());
    }

    return screens;
  }
}