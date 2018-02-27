package com.wealthfront.magellan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.view.Menu;
import android.view.ViewGroup;

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
  private Navigator navigator;
  private V view;
  private DialogCreator dialogCreator;
  private boolean dialogIsShowing;
  private Dialog dialog;
  private SparseArray<Parcelable> viewState;

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

  /**
   * @return the Navigator associated with this Screen.
   */
  public final Navigator getNavigator() {
    return navigator;
  }

  public final Dialog getDialog() {
    return dialog;
  }

  final void restore(Bundle savedInstanceState) {
    if (viewState == null && savedInstanceState != null) {
      viewState = savedInstanceState.getSparseParcelableArray(VIEW_STATE + hashCode());
    }
  }

  final V recreateView(Activity activity, Navigator navigator) {
    this.activity = activity;
    this.navigator = navigator;
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
   * Called when the Activity is resumed and when the Screen is shown.
   */
  protected void onResume(Context context) {}

  /**
   * Called when the Screen in shown (including on rotation).
   */
  protected void onShow(Context context) {}

  protected void onSave(Bundle outState) {}

  /**
   * Called when the Activity is paused and when the Screen is hidden.
   */
  protected void onPause(Context context) {}

  /**
   * Called when the Screen is hidden (including on rotation).
   */
  protected void onHide(Context context) {}

  /**
   * Finish the Activity, and therefore quit the app in a Single Activity Architecture.
   */
  protected final boolean quit() {
    if (getActivity() != null) {
      getActivity().finish();
    }
    return true;
  }

  /**
   * Allow screen to set the title with resource id after screen is shown
   */
  protected final void setTitle(@StringRes int titleResId) {
    activity.setTitle(titleResId);
  }

  protected final void setTitle(CharSequence title) {
    activity.setTitle(title);
  }

  /**
   * Allow screen to set the title after screen is shown
   */
  protected final void setTitle(CharSequence titleText) {
    activity.setTitle(titleText);
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

  @VisibleForTesting
  public final void setNavigator(Navigator navigator) {
    this.navigator = navigator;
  }

  protected final void checkOnCreateNotYetCalled(String reason) {
    checkState(activity == null, reason);
  }

}
