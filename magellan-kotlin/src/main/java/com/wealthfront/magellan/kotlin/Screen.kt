package com.wealthfront.magellan.kotlin

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.annotation.VisibleForTesting
import android.util.SparseArray
import android.view.Menu
import android.view.ViewGroup

import com.wealthfront.magellan.Preconditions.checkState
import com.wealthfront.magellan.kotlin.Preconditions.Companion.checkState

/**
 * Screens are where your logic lives (you can think of it as a Presenter in the MVP pattern, or a Controller
 * in the MVC pattern).
 *
 *
 * **Screens survive rotation.**
 *
 *
 * Here is a basic implementation of a Screen:
 * <pre> `
 * public class HomeScreen extends Screen<HomeView> {
 * @Override
 * protected HomeView createView(Context context) {
 * return new HomeView(context);
 * }
 * @Override
 * protected void onShow(Context context) {
 * getView().displaySomeData();
 * }
 * }
` *  </pre>
 */
abstract class Screen<V> : BackHandler where V : ViewGroup, V : ScreenView<*> {

  /**
   * @return the Activity associated with this Screen or null if we are not in between [.onShow] and\
   * [.onHide].
   */
  @set:VisibleForTesting
  var activity: Activity? = null
  /**
   * @return the Navigator associated with this Screen.
   */
  @set:VisibleForTesting
  var navigator: Navigator? = null
  /**
   * @return the View associated with this Screen or null if we are not in between [.onShow] and\
   * [.onHide].
   */
  @set:VisibleForTesting
  var view: V? = null
  private var dialogCreator: DialogCreator? = null
  private var dialogIsShowing: Boolean = false
  var dialog: Dialog? = null
    private set
  private var viewState: SparseArray<Parcelable>? = null

  /**
   * @return the color of the ActionBar (invalid by default).
   */
  protected val actionBarColorRes: Int
    @ColorRes
    get() = DEFAULT_ACTION_BAR_COLOR_RES

  internal fun restore(savedInstanceState: Bundle?) {
    if (viewState == null && savedInstanceState != null) {
      viewState = savedInstanceState.getSparseParcelableArray(VIEW_STATE + hashCode())
    }
  }

  internal fun recreateView(activity: Activity, navigator: Navigator): V {
    this.activity = activity
    this.navigator = navigator
    view = createView(activity)

    view!!.setScreen(this)
    if (viewState != null) {
      view!!.restoreHierarchyState(viewState)
    }
    return view
  }

  internal fun save(outState: Bundle) {
    saveViewState()
    if (viewState != null) {
      outState.putSparseParcelableArray(VIEW_STATE + hashCode(), viewState)
    }
    viewState = null
  }

  internal fun destroyView() {
    saveViewState()
    activity = null
    view = null
  }

  private fun saveViewState() {
    if (view != null) {
      viewState = SparseArray()
      view!!.saveHierarchyState(viewState)
    }
  }

  internal fun createDialog() {
    if (dialogCreator != null && dialogIsShowing) {
      dialog = dialogCreator!!.createDialog(activity)
      dialog!!.show()
    }
  }

  internal fun destroyDialog() {
    if (dialog != null) {
      dialogIsShowing = dialog!!.isShowing
      dialog!!.setOnDismissListener(null)
      dialog!!.dismiss()
      dialog = null
    }
  }

  /**
   * @return true if we should show the ActionBar, false otherwise (true by default).
   */
  protected fun shouldShowActionBar(): Boolean {
    return true
  }

  /**
   * @return true if we should animate the ActionBar, false otherwise (true by default).
   */
  protected fun shouldAnimateActionBar(): Boolean {
    return true
  }

  fun getTitle(context: Context): String {
    return ""
  }

  open fun onRestore(savedInstanceState: Bundle) {}

  /**
   * The only mandatory method to implement in a Screen. **Must** create and return a new instance of the View
   * to be displayed for this Screen.
   */
  protected abstract fun createView(context: Context): V

  /**
   * Override this method to dynamically change the menu.
   */
  protected fun onUpdateMenu(menu: Menu) {}

  /**
   * Called when the Activity is resumed and when the Screen is shown.
   */
  open fun onResume(context: Context) {}

  /**
   * Called when the Screen in shown (including on rotation).
   */
  open fun onShow(context: Context) {}

  open fun onSave(outState: Bundle) {}

  /**
   * Called when the Activity is paused and when the Screen is hidden.
   */
  open fun onPause(context: Context) {}

  /**
   * Called when the Screen is hidden (including on rotation).
   */
  open fun onHide(context: Context) {}

  /**
   * Finish the Activity, and therefore quit the app in a Single Activity Architecture.
   */
  protected fun quit(): Boolean {
    if (activity != null) {
      activity!!.finish()
    }
    return true
  }

  protected fun setTitle(@StringRes titleResId: Int) {
    activity!!.setTitle(titleResId)
  }

  protected fun setTitle(title: CharSequence) {
    activity!!.title = title
  }

  /**
   * Display a [Dialog] using a [DialogCreator]. The dialog will be automatically recreated and redisplayed
   * on rotation.
   */
  protected fun showDialog(dialogCreator: DialogCreator) {
    this.dialogCreator = dialogCreator
    this.dialogIsShowing = true
    createDialog()
  }

  /**
   * Override this method to implement a custom behavior one back pressed.
   *
   * @return true if the method consumed the back event, false otherwise.
   */
  override fun handleBack(): Boolean {
    return false
  }

  /**
   * @return a String representation of the Screen to be used for logging purposes. Return the Simple name of the class
   * by default.
   */
  override fun toString(): String {
    return javaClass.simpleName
  }

  fun checkOnCreateNotYetCalled(reason: String) {
    checkState(activity == null, reason)
  }

  companion object {

    val DEFAULT_ACTION_BAR_COLOR_RES = 0
    private val VIEW_STATE = "com.wealthfront.navigation.Screen.viewState"
  }

}
