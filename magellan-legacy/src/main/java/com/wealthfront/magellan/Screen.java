package com.wealthfront.magellan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.Menu;
import android.view.ViewGroup;

import com.wealthfront.magellan.core.Navigable;
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent;
import com.wealthfront.magellan.lifecycle.LifecycleState;
import com.wealthfront.magellan.view.DialogComponent;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.annotation.VisibleForTesting;

import static com.wealthfront.magellan.Preconditions.checkState;

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
public abstract class Screen<V extends ViewGroup & ScreenView> extends LifecycleAwareComponent implements Navigable {

  public static final int DEFAULT_ACTION_BAR_COLOR_RES = 0;
  private static final String VIEW_STATE = "com.wealthfront.navigation.Screen.viewState";

  private final DialogComponent dialogComponent = new DialogComponent();

  private Activity activity;
  private V view;
  private SparseArray<Parcelable> viewState;
  private boolean isTransitioning;
  private Queue<TransitionFinishedListener> transitionFinishedListeners = new LinkedList<>();

  public Screen() {
    attachToLifecycle(dialogComponent, LifecycleState.Destroyed.INSTANCE);
  }

  /**
   * @return the View associated with this Screen or null if we are not in between {@link #onShow(Context)} and\
   * {@link #onHide(Context)}.
   */
  public final V getView() {
    return view;
  }

  /**
   * @return the Activity associated with this Screen or null if we are not in between {@link #onShow(Context)} and\
   * {@link #onHide(Context)}.
   */
  public final Activity getActivity() {
    return activity;
  }

  final void restore(Bundle savedInstanceState) {
    if (viewState == null && savedInstanceState != null) {
      viewState = savedInstanceState.getSparseParcelableArray(VIEW_STATE + hashCode());
    }
  }

  final V recreateView(Context context) {
    this.activity = (Activity) context;
    view = createView(activity);
    // noinspection unchecked
    view.setScreen(this);
    if (viewState != null) {
      view.restoreHierarchyState(viewState);
    }
    return view;
  }

  @Override
  @CallSuper
  public void onSaveInstanceState(@NotNull Bundle outState) {
    saveViewState();
    if (viewState != null) {
      outState.putSparseParcelableArray(VIEW_STATE + hashCode(), viewState);
    }
    viewState = null;
  }

  final void destroyView() {
    saveViewState();
    activity = null;
    view = null;
  }

  private void saveViewState() {
    if (view != null) {
      viewState = new SparseArray<>();
      view.saveHierarchyState(viewState);
    }
  }

  @Override
  @CallSuper
  public void transitionStarted() {
    isTransitioning = true;
    transitionFinishedListeners.clear();
  }

  @Override
  @CallSuper
  public void transitionFinished() {
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
   * @return true if we should show the ActionBar, false otherwise (true by default).
   */
  protected boolean shouldShowActionBar() {
    return true;
  }

  /**
   * @return true if we should animate the ActionBar, false otherwise (true by default).
   */
  protected boolean shouldAnimateActionBar() {
    return true;
  }

  public String getTitle(Context context) {
    return "";
  }

  /**
   * @return the color of the ActionBar (invalid by default).
   */
  @ColorRes
  protected int getActionBarColorRes() {
    return DEFAULT_ACTION_BAR_COLOR_RES;
  }

  protected void onRestore(Bundle savedInstanceState) {}

  /**
   * The only mandatory method to implement in a Screen. <b>Must</b> create and return a new instance of the View
   * to be displayed for this Screen.
   */
  protected abstract V createView(Context context);

  /**
   * Override this method to dynamically change the menu.
   */
  protected void onUpdateMenu(Menu menu) {}

  /**
   * Called when the Screen in shown (including on rotation).
   */
  @Override
  @CallSuper
  protected void onShow(@NotNull Context context) {
    recreateView(context);
  }

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
  @CallSuper
  protected void onHide(@NotNull Context context) {
    destroyView();
  }

  /**
   * Called when the activity is killed.
   */
  @Override
  @CallSuper
  protected void onDestroy(@NotNull Context context) { }

  /**
   * Finish the Activity, and therefore quit the app in a Single Activity Architecture.
   */
  protected final boolean quit() {
    if (getActivity() != null) {
      getActivity().finish();
    }
    return true;
  }

  protected final void setTitle(@StringRes int titleResId) {
    activity.setTitle(titleResId);
  }

  protected final void setTitle(CharSequence title) {
    activity.setTitle(title);
  }

  /**
   * Display a {@link Dialog} using a {@link DialogCreator}. The dialog will be automatically recreated and redisplayed
   * on rotation.
   */
  protected final void showDialog(DialogCreator dialogCreator) {
    dialogComponent.showDialog(dialogCreator);
  }

  /**
   * @return a String representation of the Screen to be used for logging purposes. Return the Simple name of the class
   * by default.
   */
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @VisibleForTesting
  public final void setView(V view) {
    this.view = view;
  }

  @VisibleForTesting
  public final void setActivity(Activity activity) {
    this.activity = activity;
  }

  protected final void checkOnCreateNotYetCalled(String reason) {
    checkState(activity == null, reason);
  }

  @Override
  @CallSuper
  protected void onCreate(@NotNull Context context) {
    this.activity = (Activity) context;
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
