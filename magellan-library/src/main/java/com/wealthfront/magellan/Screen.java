package com.wealthfront.magellan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.Menu;
import android.view.ViewGroup;

import com.wealthfront.magellan.lifecycle.LifecycleListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
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
public abstract class Screen<V extends ViewGroup & ScreenView> implements BackHandler {

  public static final int DEFAULT_ACTION_BAR_COLOR_RES = 0;
  private static final String VIEW_STATE = "com.wealthfront.navigation.Screen.viewState";

  private Activity activity;
  private V view;
  private DialogCreator dialogCreator;
  private boolean dialogIsShowing;
  private Dialog dialog;
  private SparseArray<Parcelable> viewState;
  private boolean isTransitioning;
  private Queue<TransitionFinishedListener> transitionFinishedListeners = new LinkedList<>();
  private List<LifecycleListener> lifecycleListeners = new ArrayList<>();

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

  public final Dialog getDialog() {
    return dialog;
  }

  final void restore(Bundle savedInstanceState) {
    if (viewState == null && savedInstanceState != null) {
      viewState = savedInstanceState.getSparseParcelableArray(VIEW_STATE + hashCode());
    }
    onRestore(savedInstanceState);
    for (LifecycleListener lifecycleListener : lifecycleListeners) {
      lifecycleListener.onRestore(savedInstanceState);
    }
  }

  final V recreateView(Activity activity) {
    this.activity = activity;
    view = createView(activity);
    // noinspection unchecked
    view.setScreen(this);
    if (viewState != null) {
      view.restoreHierarchyState(viewState);
    }
    return view;
  }

  final void save(Bundle outState) {
    saveViewState();
    if (viewState != null) {
      outState.putSparseParcelableArray(VIEW_STATE + hashCode(), viewState);
    }
    onSave(outState);
    for (LifecycleListener lifecycleListener : lifecycleListeners) {
      lifecycleListener.onSave(outState);
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

  final void createDialog() {
    if (dialogCreator != null && dialogIsShowing) {
      dialog = dialogCreator.createDialog(activity);
      dialog.show();
    }
  }

  final void destroyDialog() {
    if (dialog != null) {
      dialogIsShowing = dialog.isShowing();
      dialog.setOnDismissListener(null);
      dialog.dismiss();
      dialog = null;
    }
  }

  void transitionStarted() {
    isTransitioning = true;
    transitionFinishedListeners.clear();
  }

  void transitionFinished() {
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

  final void show(Context context) {
    for (LifecycleListener lifecycleListener: lifecycleListeners) {
      lifecycleListener.onShow(context);
    }
    onShow(context);
  }

  final void resume(Context context) {
    for (LifecycleListener lifecycleListener: lifecycleListeners) {
      lifecycleListener.onResume(context);
    }
    onResume(context);
  }

  final void pause(Context context) {
    onPause(context);
    for (LifecycleListener lifecycleListener: lifecycleListeners) {
      lifecycleListener.onPause(context);
    }
  }
  
  final void hide(Context context) {
    onHide(context);
    for (LifecycleListener lifecycleListener: lifecycleListeners) {
      lifecycleListener.onHide(context);
    }
  }

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
   * Called when the Activity is resumed and when the Screen is shown.
   */
  protected void onResume(Context context) {}

  /**
   * Called when the Screen in shown (including on rotation).
   */
  protected void onShow(Context context) {}

  /**
   * Called when the Activity is paused and when the Screen is hidden.
   */
  protected void onPause(Context context) {}

  /**
   * Called when the Screen is hidden (including on rotation).
   */
  protected void onHide(Context context) {}

  protected void onRestore(Bundle savedInstanceState) {}

  protected void onSave(Bundle outState) {}

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
    this.dialogCreator = dialogCreator;
    this.dialogIsShowing = true;
    createDialog();
  }

  /**
   * Override this method to implement a custom behavior one back pressed.
   *
   * @return true if the method consumed the back event, false otherwise.
   */
  @Override
  public boolean handleBack() {
    return false;
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

  public final void addLifecycleListener(@NonNull LifecycleListener lifecycleListener) {
    lifecycleListeners.add(lifecycleListener);
  }

  public final void removeLifecycleListener(@NonNull LifecycleListener lifecycleListener) {
    lifecycleListeners.remove(lifecycleListener);
  }

  public final void clearLifecycleListeners() {
    lifecycleListeners.clear();
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
