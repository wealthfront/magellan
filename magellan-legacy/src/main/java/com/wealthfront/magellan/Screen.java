package com.wealthfront.magellan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.coroutines.ShownLifecycleScope;
import com.wealthfront.magellan.init.Magellan;
import com.wealthfront.magellan.lifecycle.LifecycleAware;
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent;
import com.wealthfront.magellan.navigation.NavigableCompat;
import com.wealthfront.magellan.view.DialogComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.VisibleForTesting;
import kotlinx.coroutines.CoroutineScope;

/**
 * Screens are where your logic lives (you can think of it as a Presenter in the MVP pattern, or a Controller
 * in the MVC pattern).
 * <p>
 * <b>Screens survive rotation.</b>
 * <p>
 * Here is a basic implementation of a Screen:
 * <pre> <code>
 * public class HomeScreen extends Screen&lt;HomeView&gt; {
 *  {@literal @}Override
 *   protected HomeView createView(Context context) {
 *     return new HomeView(context);
 *   }
 *  {@literal @}Override
 *   protected void onShow(Context context) {
 *     getView().displaySomeData();
 *   }
 * }
 * </code> </pre>
 */
public abstract class Screen<V extends ViewGroup & ScreenView> extends LifecycleAwareComponent implements NavigableCompat {

  private final DialogComponent dialogComponent = new DialogComponent();
  private final ShownLifecycleScope shownScope = Magellan.getShownScopeProvider().invoke();
  private CoroutineScope overrideScope = null;

  private @Nullable Activity activity;
  private @Nullable V view;
  private boolean isTransitioning;
  private final Queue<TransitionFinishedListener> transitionFinishedListeners = new LinkedList<>();
  private Navigator navigator;

  public Screen() {
    attachToLifecycle(new LegacyViewComponent<>(this));
    attachToLifecycle(dialogComponent);
    attachToLifecycle(shownScope);
  }

  /**
   * @return the View associated with this Screen or null if we are not in between {@link #onShow(Context)} and\
   * {@link #onHide(Context)}.
   */
  @Nullable
  public V getView() {
    return view;
  }

  /**
   * @return the Activity associated with this Screen or null if we are not in between {@link #onShow(Context)} and
   * {@link #onHide(Context)}.
   */
  @Nullable
  public final Activity getActivity() {
    return activity;
  }

  /**
   * @return the Navigator associated with this Screen.
   */
  @NotNull
  public final Navigator getNavigator() {
    return navigator;
  }

  public final Dialog getDialog() {
    return dialogComponent.getDialog();
  }

  public final CoroutineScope getShownScope() {
    if (overrideScope != null) {
      return overrideScope;
    } else {
      return shownScope;
    }
  }

  @VisibleForTesting
  public final void setShownScope(CoroutineScope newScope) {
    overrideScope = newScope;
  }

  @Override
  public final void transitionStarted() {
    isTransitioning = true;
    transitionFinishedListeners.clear();
  }

  @Override
  public final void transitionFinished() {
    isTransitioning = false;
    while (transitionFinishedListeners.size() > 0) {
      transitionFinishedListeners.remove().onTransitionFinished();
    }
  }

  /**
   * Adds a {@link TransitionFinishedListener} to be called when the navigation transition into this screen is finished,
   * or immediately if the transition is already finished.
   * @param listener The listener to be called when the transition is finished or immediately.
   */
  protected final void whenTransitionFinished(TransitionFinishedListener listener) {
    if (isTransitioning) {
      transitionFinishedListeners.add(listener);
    } else {
      listener.onTransitionFinished();
    }
  }

  /**
   * The only mandatory method to implement in a Screen. <b>Must</b> create and return a new instance of the View
   * to be displayed for this Screen.
   */
  protected abstract V createView(@NotNull Context context);

  /**
   * Called when the Screen is navigated to from before the screen is shown (not triggered on rotation).
   */
  @Override
  protected void onCreate(@NotNull Context context) { }

  /**
   * Called when the Screen in shown (including on rotation).
   */
  @Override
  protected void onShow(@NotNull Context context) { }

  /**
   * Called when the activity is resumed and when the Screen is shown.
   */
  @Override
  protected void onResume(@NotNull Context context) { }

  /**
   * Called when the activity is paused and when the Screen is hidden.
   */
  @Override
  protected void onPause(@NotNull Context context) { }

  /**
   * Called when the Screen is hidden (including on rotation).
   */
  @Override
  protected void onHide(@NotNull Context context) { }

  /**
   * Called when the Screen is navigated away from after the screen is hidden (not triggered on rotation).
   */
  @Override
  protected void onDestroy(@NotNull Context context) { }

  /**
   * Override this method to implement a custom behavior one back pressed.
   *
   * @return true if the method consumed the back event, false otherwise.
   */
  public boolean handleBack() {
    return false;
  }

  @Override
  protected boolean onBackPressed() {
    return handleBack();
  }

  /**
   * Finish the Activity, and therefore quit the app in a Single Activity Architecture.
   */
  @SuppressLint("AvoidUsingActivity")
  public boolean quit() {
    if (activity != null) {
      activity.finish();
    }
    return true;
  }

  /**
   * Display a {@link Dialog} using a {@link DialogCreator}. The dialog will be automatically recreated and redisplayed
   * on rotation.
   */
  public void showDialog(@NotNull DialogCreator dialogCreator) {
    dialogComponent.showDialog(dialogCreator);
  }

  /**
   * @return a String representation of the Screen to be used for logging purposes. Return the Simple name of the class
   * by default.
   */
  @NotNull
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  public final void setView(@Nullable V view) {
    this.view = view;
  }

  public final void setActivity(@Nullable Activity activity) {
    this.activity = activity;
    this.dialogComponent.setContext(activity);
  }

  public final void setNavigator(@NotNull Navigator navigator) {
    this.navigator = navigator;
  }

  /**
   * A simple interface with a method to be run when the transition to this screen is finished, or immediately if it's
   * already finished.
   */
  public interface TransitionFinishedListener {

    /**
     * The method to run when the transition to this screen is finished.
     */
    void onTransitionFinished();

  }

}
