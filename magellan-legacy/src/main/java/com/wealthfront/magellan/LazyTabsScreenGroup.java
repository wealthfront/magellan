package com.wealthfront.magellan;

import android.content.Context;
import android.view.Menu;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleAware;
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.VisibleForTesting;

/**
 * A Screen containing a list of screens which are lazily added to the lifecycle when [setSelectedScreen] method is called.
 * Useful to display reusable Screens that can be either in another one or on it's own.
 */
public abstract class LazyTabsScreenGroup<S extends Screen, V extends ViewGroup & ScreenView> extends Screen<V> implements MultiScreen<S> {

  private List<S> lazyScreens;
  private S selectedLazyScreen;

  public LazyTabsScreenGroup() {
    this(new ArrayList<>());
  }

  public LazyTabsScreenGroup(@NotNull List<S> screens) {
    this.lazyScreens = new ArrayList<>();
    addScreens(screens);
  }

  @Override
  public void addScreen(@NotNull S screen) {
    attachToLifecycleWithNavigator(screen);
    lazyScreens.add(screen);
    if (selectedLazyScreen == null) {
      selectedLazyScreen = lazyScreens.get(0);
    }
  }

  @Override
  public void addScreens(@NotNull List<S> screens) {
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

  private S findLazyScreen(S screen) {
    for (S lazyScreen : lazyScreens) {
      if (lazyScreen == screen) {
        return lazyScreen;
      }
    }
    throw new IllegalArgumentException("Screen " + screen.getClass().getSimpleName() + " doesn't exist");
  }

  public void setSelectedScreen(S screen) {
    S lazyScreen = findLazyScreen(screen);
    if (selectedLazyScreen == lazyScreen || getActivity() == null) {
      return;
    }
    selectedLazyScreen = lazyScreen;
    getActivity().invalidateOptionsMenu();
    showSelectedScreen();
    getActivity().setTitle(getTitle(getActivity()));
  }

  public S getSelectedScreen() {
    return selectedLazyScreen;
  }

  @Override
  public void onUpdateMenu(@NotNull Menu menu) {
    selectedLazyScreen.onUpdateMenu(menu);
  }

  @Override
  protected void onShow(@NotNull Context context) {
    showSelectedScreen();
  }

  @VisibleForTesting
  protected void showSelectedScreen() {
    if (selectedLazyScreen.getCurrentState() == Destroyed.INSTANCE) {
      attachToLifecycle(selectedLazyScreen, Destroyed.INSTANCE);
    }
    onScreenDisplayed(selectedLazyScreen);
  }

  public abstract void onScreenDisplayed(S screen);

  @NotNull
  @Override
  public String getTitle(@NotNull Context context) {
    return selectedLazyScreen.getTitle(context);
  }

  @Override
  protected void onHide(@NotNull Context context) {
    for (S screen : lazyScreens) {
      if (screen.getCurrentState() != Destroyed.INSTANCE) {
        removeFromLifecycle(screen, Destroyed.INSTANCE);
      }
    }
  }

  @Override
  public final List<S> getScreens() {
    return lazyScreens;
  }
}