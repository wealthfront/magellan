package com.wealthfront.magellan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.wealthfront.magellan.transitions.DefaultTransition;
import com.wealthfront.magellan.transitions.Transition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;
import static com.wealthfront.magellan.NavigationType.GO;
import static com.wealthfront.magellan.NavigationType.NO_ANIM;
import static com.wealthfront.magellan.NavigationType.SHOW;
import static com.wealthfront.magellan.Views.whenMeasured;
import static com.wealthfront.magellan.Preconditions.checkArgument;
import static com.wealthfront.magellan.Preconditions.checkNotNull;
import static com.wealthfront.magellan.Preconditions.checkState;

/**
 * Class responsible for navigating between screens and maintaining collection of screens in a back stack.
 */
public class Navigator implements BackHandler {

  private final Deque<Screen> backStack = new ArrayDeque<>();
  private Activity activity;
  private Menu menu;
  private ScreenContainer container;
  private final Transition transition;
  private Transition overridingTransition;
  private final List<ScreenLifecycleListener> lifecycleListeners = new ArrayList<>();
  private View ghostView; // keep track of the disappearing view we are animating
  private boolean loggingEnabled;
  private EventTracker eventTracker;

  public static Builder withRoot(Screen root) {
    return new Builder(root);
  }

  Navigator(Builder builder) {
    backStack.push(builder.root);
    transition = builder.transition;
    loggingEnabled = builder.loggingEnabled;
    eventTracker = new EventTracker(builder.maxEventsTracked);
  }

  /**
   * Adds a lifecycle listener that will be notified when each Screen is shown and hidden.
   *
   * @param lifecycleListener  Listener that receives screen lifecycle events
   */
  public void addLifecycleListener(ScreenLifecycleListener lifecycleListener) {
    lifecycleListeners.add(lifecycleListener);
  }

  /**
   * Unregisters a Screen lifecycle listener. Removed listener will no longer receive screen lifecycle
   * callbacks.
   *
   * @param lifecycleListener  listener to unregister from receiving screen lifecycle events
   */
  public void removeLifecycleListener(ScreenLifecycleListener lifecycleListener) {
    lifecycleListeners.remove(lifecycleListener);
  }

  /**
   * Initializes the Navigator with Activity instance and Bundle for saved instance state.
   *
   * Call this method from {@link Activity#onCreate(Bundle) onCreate} of the Activity associated with this Navigator.
   *
   * @param activity  Activity associated with application
   * @param savedInstanceState  state to restore from previously destroyed activity
   * @throws IllegalStateException when no {@link ScreenContainer} view present in hierarchy with view id of container
   */
  public void onCreate(Activity activity, Bundle savedInstanceState) {
    this.activity = activity;
    container = (ScreenContainer) activity.findViewById(R.id.magellan_container);
    checkState(container != null, "There must be a ScreenContainer whose id is R.id.magellan_container in the view hierarchy");
    for (Screen screen : backStack) {
      screen.restore(savedInstanceState);
      screen.onRestore(savedInstanceState);
    }
    showCurrentScreen(FORWARD);
  }

  /**
   * Notifies all screens to save instance state in input Bundle.
   *
   * Call this method from {@link Activity#onSaveInstanceState(Bundle) onSaveInstanceState} in the Activity associated
   * with this Navigator
   *
   * @param outState  Bundle in which to store screen state information
   */
  public void onSaveInstanceState(Bundle outState) {
    for (Screen screen : backStack) {
      screen.save(outState);
      screen.onSave(outState);
    }
  }

  /**
   * Attaches options menu to Navigator to allow Screens to control which items are shown/hidden.
   *
   * All menu items are hidden by default.
   *
   * Call this method from {@link Activity#onCreateOptionsMenu(Menu) onCreateOptionsMenu} in the Activity associated
   * with this Navigator
   *
   * @param menu  options menu to be controlled by current screen
   */
  public void onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    updateMenu();
  }

  /**
   * Updates options menu to Navigator to allow Screens to update which items are shown/hidden.
   *
   * All menu items are hidden by default.
   *
   * Call this method from {@link Activity#onPrepareOptionsMenu(Menu) onPrepareOptionsMenu} in the Activity associated
   * with this Navigator
   *
   * @param menu  options menu to be updated by current screen
   */
  public void onPrepareOptionsMenu(Menu menu) {
    this.menu = menu;
    updateMenu();
  }

  /**
   * Notifies Navigator that the activity's onResume lifecycle callback has been hit. Call this method from
   * {@code onResume} of the Activity associated with this Navigator.
   *
   * This method will notify the current screen of the lifecycle event if the activity parameter is the same as the
   * activity provided to this Navigator in {@link #onCreate(Activity, Bundle) onCreate}.
   *
   * @param activity  activity that received onResume callback
   */
  public void onResume(Activity activity) {
    if (sameActivity(activity)) {
      currentScreen().onResume(activity);
    }
  }

  /**
   * Notifies Navigator that the activity's onPause lifecycle callback has been hit. Call this method from
   * {@link Activity#onPause() onPause} of the Activity associated with this Navigator.
   *
   * This method will notify the current screen of the lifecycle event if the activity parameter is the same as the
   * activity provided to this Navigator in {@link #onCreate(Activity, Bundle) onCreate}.
   *
   * @param activity  activity that received onPause callback
   */
  public void onPause(Activity activity) {
    if (sameActivity(activity)) {
      currentScreen().onPause(activity);
    }
  }

  /**
   * Notifies Navigator that the activity's onDestroy lifecycle callback has been hit. Call this method from
   * {@link Activity#onDestroy() onDestroy} of the Activity associated with this Navigator.
   *
   * This method will hid the current screen, and clear references to this Navigators associated activity, menu, and
   * container view, if the activity parameter is the same as the activity provided to this Navigator in
   * {@link #onCreate(Activity, Bundle) onCreate}.
   *
   * @param activity  activity that received onDestroy callback
   */
  public void onDestroy(Activity activity) {
    if (sameActivity(activity)) {
      hideCurrentScreen();
      this.activity = null;
      container = null;
      menu = null;
    }
  }

  /**
   * Handles back button press by user. Notifies current screen on back button press and allows screen to handle
   * back press itself. If the current screen does not handle back press, and this Navigator has more than one screen,
   * this Navigator will remove the current screen from its collection of screens and display the previous screen.
   * If this Navigator only has one Screen when it receives a call to {@link #handleBack()} it will not handle the back
   * button press.
   *
   * Call this method from within {@link Activity#onBackPressed()} of the activity associated with this Navigator.
   *
   * (Example usage) In the Activity class associated with this Navigator, put:
   * <pre> <code>
   * {@literal @}Override
   * public void onBackPressed() {
   *   if (!navigator.handleBack()) {
   *     super.onBackPressed();
   *   }
   * }
   * </code> </pre>
   *
   * @return true if the Navigator consumed the back button click
   */
  @Override
  public boolean handleBack() {
    Screen currentScreen = currentScreen();
    if (currentScreen.handleBack()) {
      return true;
    } else {
      if (!atRoot()) {
        goBack();
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Gets the screen on top of this Navigator's back stack.
   *
   * @return the current screen in the back stack
   */
  public Screen currentScreen() {
    checkBackStackNotEmpty();
    return backStack.peek();
  }

  /**
   * Returns true if this Navigator is at its root screen, false otherwise.
   *
   * @return true if this Navigator is at its root screen, false otherwise.
   */
  public boolean atRoot() {
    return backStack.size() == 1;
  }

  /**
   * Clears the back stack of this Navigator and adds the input Screen as the new root of the back stack.
   *
   * @param activity  activity used to verify this Navigator is in an acceptable state when resetWithRoot is called
   * @param root  new root screen for this Navigator
   * @throws IllegalStateException if {@link #onCreate(Activity, Bundle)} has already been called on this Navigator
   */
  public void resetWithRoot(Activity activity, final Screen root) {
    checkOnCreateNotYetCalled(activity, "resetWithRoot() must be called before onCreate()");
    backStack.clear();
    backStack.push(root);
  }

  /**
   * Change the elements of the back stack according to the implementation of the HistoryRewriter parameter.
   * <b>Note, this method cannot be called after calling {@link #onCreate(Activity, Bundle)} on this Navigator.</b> The
   * primary use case for this method is to change the back stack before the navigator is fully initialized (e.g.
   * showing a login screen if necessary). It is possible to manipulate the back stack with a {@link HistoryRewriter},
   * {@link #navigate(HistoryRewriter)}.
   *
   * @param activity  activity used to verify this Navigator is in an acceptable state when resetWithRoot is called
   * @param historyRewriter  rewrites back stack to desired state
   * @throws IllegalStateException if {@link #onCreate(Activity, Bundle)} has already been called on this Navigator
   */
  public void rewriteHistory(Activity activity, HistoryRewriter historyRewriter) {
    checkOnCreateNotYetCalled(activity, "rewriteHistory() must be called before onCreate()");
    historyRewriter.rewriteHistory(backStack);
  }

  /**
   * Navigates to the screen on top of the back stack after the HistoryRewriter has rewritten the back stack history.
   *
   * Does not animate during the magellan.
   *
   * @param historyRewriter  manipulates the back stack during magellan
   */
  public void navigate(final HistoryRewriter historyRewriter) {
    navigate(historyRewriter, NO_ANIM);
  }

  /**
   * Navigates to the screen on top of the back stack after the HistoryRewriter has rewritten the back stack history.
   *
   * Animates the magellan according to the NavigationType parameter.
   *
   * @param historyRewriter  manipulates the back stack during magellan
   * @param navType  controls how the new screen is displayed to the user
   */
  public void navigate(final HistoryRewriter historyRewriter, NavigationType navType) {
    navigate(FORWARD, navType, new Runnable() {
      @Override
      public void run() {
        historyRewriter.rewriteHistory(backStack);
      }
    });
  }

  /**
   * Navigates to the screen on top of the back stack after the HistoryRewriter has rewritten the back stack history.
   *
   * Animates the magellan according to the NavigationType parameter, in the direction specified by the Direction
   * parameter.
   *
   * @param historyRewriter  manipulates the back stack during magellan
   * @param navType  controls how the new screen is displayed to the user
   * @param direction  controls the direction in which the new screen moves in and old screen moves out during magellan
   */
  public void navigate(final HistoryRewriter historyRewriter, NavigationType navType, Direction direction) {
    navigate(direction, navType, new Runnable() {
      @Override
      public void run() {
        historyRewriter.rewriteHistory(backStack);
      }
    });
  }

  /**
   * Replaces screen on top of the back stack with the new screen. When magellan completes, previous screen on top of
   * the back stack will have been removed, and the Screen parameter will be the new top screen on the back stack.
   *
   * @param screen  new top screen on back stack
   */
  public void replace(Screen screen) {
    replace(screen, GO);
  }

  /**
   * Replaces screen on top of the back stack with the new screen without animation. When magellan completes,
   * previous screen on top of the back stack will have been removed, and the Screen parameter will be the new top
   * screen on the back stack.
   *
   * @param screen  new top screen on back stack
   */
  public void replaceNow(Screen screen) {
    replace(screen, NO_ANIM);
  }

  /**
   * Shows new screen by animating screen to slide up from bottom of container to cover previous screen. When magellan
   * completes, the Screen parameter will be on top of this Navigator's back stack.
   *
   * If the current screen is already being shown, as determined by {@code currentScreen().equals(screen)}, this method
   * will do nothing.
   *
   * @param screen  new top screen on back stack
   */
  public void show(Screen screen) {
    show(screen, SHOW);
  }

  /**
   * Shows new screen without animating the magellan event. When magellan completes, the Screen parameter will be
   * on top of this Navigator's back stack.
   *
   * If the current screen is already being shown, this method will do nothing.
   *
   * @param screen  new top screen on back stack
   */
  public void showNow(Screen screen) {
    show(screen, NO_ANIM);
  }

  private void show(Screen screen, NavigationType navType) {
    if (!isCurrentScreen(screen)) {
      navigateTo(screen, navType);
    }
  }

  /**
   * Navigates back to previous screen if Screen parameter is currently the top screen on this Navigator's back stack.
   * If the Screen parameter is not the top screen on the back stack, this method does nothing.
   *
   * @param screen  screen to hide
   */
  public void hide(Screen screen) {
    if (isCurrentScreen(screen)) {
      navigateBack(SHOW);
    }
  }

  /**
   * Returns true if the Screen parameter is the current screen on top of this Navigator's back stack, false otherwise.
   * Equality is determined using {@code Screen#equals(Object)}.
   *
   * @param screen screen to check if it is currently on top of this Navigator's back stack
   * @return true if the Screen parameter is the top screen on this Navigator's back stack
   */
  public boolean isCurrentScreen(Screen screen) {
    return !backStack.isEmpty() && currentScreen().equals(screen);
  }

  /**
   * Navigates to the Screen parameter. Animates current top screen on this Navigator's back stack out to the left and
   * slides the Screen parameter screen in from the right. At the end of the magellan event, the Screen parameter will
   * be the top screen on this Navigator's back stack.
   *
   * @param screen  new top screen for this Navigator's back stack
   */
  public void goTo(Screen screen) {
    navigateTo(screen, GO);
  }

  /**
   * Navigates from current screen to previous screen in this Navigator's back stack. Current screen animates out of the
   * view by sliding out to the right and the next screen in the back stack slides in from the left.
   *
   * @throws IllegalStateException if this Navigator only has one screen in its back stack
   */
  public void goBack() {
    checkState(!atRoot(), "Can't go back, this is the last screen. Did you mean to call handleBack() instead?");
    navigateBack(GO);
  }

  /**
   * Navigates from current screen all the way to the root screen in this Navigator's back stack, removing all
   * intermediate screen in this Navigator's back stack along the way. The current screen animates out of the view
   * according to the animation specified by the NavigationType parameter.
   *
   * @param magellanType  determines how the magellan event is animated
   */
  public void goBackToRoot(NavigationType magellanType) {
    navigate(new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
        while (history.size() > 1) {
          history.pop();
        }
      }
    }, magellanType, BACKWARD);
  }

  /**
   * Navigates from the current screen back to the Screen parameter wherever it is in this Navigator's back stack.
   * Screens in between the current screen and the Screen parameter on the back stack are removed. If the Screen
   * parameter is not present in this Navigator's back stack, this method is equivalent to
   * {@link #goBackToRoot(NavigationType) goBackToRoot(NavigationType.GO)}
   *
   * @param screen  screen to navigate back to through this Navigator's back stack
   */
  public void goBackTo(final Screen screen) {
    navigate(new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
        checkArgument(history.contains(screen), "Can't go back to a screen that isn't in history.");
        while (history.size() > 1) {
          if (history.peek() == screen) {
            break;
          }
          history.pop();
        }
      }
    }, GO, BACKWARD);
  }

  /**
   * Navigates from the current screen back to the screen in this Navigator's back stack immediately before the
   * Screen parameter. Screens in between the current screen and the Screen parameter on the back stack are removed.
   * If the Screen parameter is not present in this Navigator's back stack, this method is equivalent to
   * {@link #goBackToRoot(NavigationType) goBackToRoot(NavigationType.GO)}
   *
   * @param screen  screen to navigate back to through this Navigator's back stack
   */
  public void goBackBefore(Screen screen) {
    goBackBefore(screen, GO);
  }

  /**
   * Navigates from the current screen back to the screen in this Navigator's back stack immediately before the
   * Screen parameter. Screens in between the current screen and the Screen parameter on the back stack are removed.
   * If the Screen parameter is not present in this Navigator's back stack, this method is equivalent to
   * {@link #goBackToRoot(NavigationType) goBackToRoot(NavigationType)}
   *
   * @param screen  screen to navigate back to through this Navigator's back stack
   * @param magellanType  determines how the magellan event is animated
   */
  public void goBackBefore(final Screen screen, NavigationType magellanType) {
    navigate(new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
        checkArgument(history.contains(screen), "Can't go back past a screen that isn't in history.");
        while (history.size() > 1) {
          if (history.pop() == screen) {
            break;
          }
        }
      }
    }, magellanType, BACKWARD);
  }

  private void replace(final Screen screen, NavigationType navType) {
    navigate(FORWARD, navType, new Runnable() {
      @Override
      public void run() {
        backStack.pop();
        backStack.push(screen);
      }
    });
  }

  private void navigateTo(final Screen screen, NavigationType navType) {
    navigate(FORWARD, navType, new Runnable() {
      @Override
      public void run() {
        backStack.push(screen);
      }
    });
  }

  private void navigateBack(NavigationType navType) {
    navigate(BACKWARD, navType, new Runnable() {
      @Override
      public void run() {
        backStack.pop();
      }
    });
  }

  private void navigate(final Direction direction, final NavigationType navType, final Runnable backStackOperation) {
    container.setInterceptTouchEvents(true);
    checkNotNull(activity, "The activity cannot be null. Did you forget to call onCreate()?");
    currentScreen().onPause(activity);
    View from = hideCurrentScreen();
    backStackOperation.run();
    View to = showCurrentScreen(direction);
    currentScreen().onResume(activity);
    animateAndRemove(from, to, navType, direction);
    reportEvent(navType, direction);
  }

  private void animateAndRemove(final View from, final View to, final NavigationType navType, final Direction direction) {
    ghostView = from;
    final Transition transitionToUse = overridingTransition != null ? overridingTransition : transition;
    overridingTransition = null;
    whenMeasured(to, new Views.OnMeasured() {
      @Override
      public void onMeasured() {
        currentScreen().transitionStarted();
        transitionToUse.animate(from, to, navType, direction, new Transition.Callback() {
          @Override
          public void onAnimationEnd() {
            if (container != null) {
              container.removeView(from);
              if (from == ghostView) {
                // Only clear the ghost if it's the same as the view we just removed
                ghostView = null;
              }
              currentScreen().transitionFinished();
              container.setInterceptTouchEvents(false);
            }
          }
        });
      }
    });
  }

  private boolean isAnimating() {
    return ghostView != null;
  }

  private View showCurrentScreen(Direction direction) {
    Screen currentScreen = currentScreen();
    View view = currentScreen.recreateView(activity, this);
    container.addView(view, direction == FORWARD ? container.getChildCount() : 0);
    currentScreen.createDialog();
    activity.setTitle(currentScreen.getTitle(activity));
    currentScreen.onShow(activity);
    for (ScreenLifecycleListener lifecycleListener : lifecycleListeners) {
      lifecycleListener.onShow(currentScreen);
    }
    callOnNavigate(currentScreen);
    // Need to post to avoid animation bug on disappearing menu
    new Handler(getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        updateMenu();
      }
    });
    return view;
  }

  private View hideCurrentScreen() {
    // if we were already animating a view, just skip it and remove the view immediately
    if (isAnimating()) {
      container.removeView(ghostView);
      ghostView = null;
    }
    checkState(container.getChildCount() == 1, "The container view must have a single child, but it had " + container.getChildCount());

    Screen currentScreen = currentScreen();
    for (ScreenLifecycleListener lifecycleListener : lifecycleListeners) {
      lifecycleListener.onHide(currentScreen);
    }
    currentScreen.onHide(activity);
    currentScreen.destroyDialog();
    currentScreen.destroyView();
    View view = container.getChildAt(0); // will be removed at the end of the animation
    return view;
  }

  private void callOnNavigate(Screen currentScreen) {
    if (activity instanceof NavigationListener) {
      ((NavigationListener) activity).onNavigate(
          ActionBarConfig.with()
              .visible(currentScreen.shouldShowActionBar())
              .animated(currentScreen.shouldAnimateActionBar())
              .colorRes(currentScreen.getActionBarColorRes())
              .build());
    }
  }

  private void updateMenu() {
    if (menu != null) {
      for (int i = 0; i < menu.size(); i++) {
        menu.getItem(i).setVisible(false);
      }
      currentScreen().onUpdateMenu(menu);
    }
  }

  private boolean sameActivity(Activity activity) {
    return this.activity == activity;
  }

  private void checkOnCreateNotYetCalled(Activity activity, String reason) {
    checkState(this.activity == null || !sameActivity(activity), reason);
  }

  private void checkBackStackNotEmpty() {
    checkState(!backStack.isEmpty(), "There must be a least one screen in the backstack");
  }

  private void reportEvent(NavigationType navType, Direction direction) {
    Event event = new Event(navType, direction, getBackStackDescription());
    eventTracker.reportEvent(event);
    if (loggingEnabled) {
      Log.d(Navigator.class.getSimpleName(), event.toString());
    }
  }

  /**
   * Returns a human-readable string describing the screens in this Navigator's back stack.
   *
   * @return a human-readable description of the back stack.
   */
  public String getBackStackDescription() {
    ArrayList<Screen> backStackCopy = new ArrayList<>(backStack);
    Collections.reverse(backStackCopy);
    String currentScreen = "";
    if (!backStackCopy.isEmpty()) {
      currentScreen = backStackCopy.remove(backStackCopy.size() - 1).toString();
    }
    return TextUtils.join(" > ", backStackCopy) + (backStackCopy.isEmpty() ? "" : " > ") + "[" + currentScreen + "]";
  }

  /**
   * Returns a human-readable string describing the magellan events that happened recently
   * (including the state of the backStack at the time).
   * How many events are kept can be configured using {@link Builder#maxEventsTracked(int)} (the default is 50).
   *
   * @return a human-readable description of the past events.
   */
  public String getEventsDescription() {
    return eventTracker.getEventsDescription();
  }

  /**
   * Sets a specific transition to use during the next magellan event. The overriding transition will only be used
   * once, subsequent magellan events will use the default transition specified during construction of this Navigator.
   *
   * @param overridingTransition  transition to override default
   * @return this Navigator that will use the Transition param for its next magellan event
   */
  public Navigator overrideTransition(Transition overridingTransition) {
    this.overridingTransition = overridingTransition;
    return this;
  }

  /**
   * Builder for constructing Navigators with particular parameters.
   *
   * Use {@link #withRoot(Screen)} to create a Builder, which can then be used to construct a navigator.
   */
  public static class Builder {

    private final Screen root;
    private Transition transition = new DefaultTransition();
    private boolean loggingEnabled;
    private int maxEventsTracked = 50;

    Builder(Screen root) {
      this.root = root;
    }

    public Builder transition(Transition transition) {
      this.transition = transition;
      return this;
    }

    public Builder loggingEnabled(boolean loggingEnabled) {
      this.loggingEnabled = loggingEnabled;
      return this;
    }

    public Builder maxEventsTracked(int maxEventsTracked) {
      this.maxEventsTracked = maxEventsTracked;
      return this;
    }

    public Navigator build() {
      return new Navigator(this);
    }

  }

}
