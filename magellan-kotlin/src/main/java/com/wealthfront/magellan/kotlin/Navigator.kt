package com.wealthfront.magellan.kotlin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.View
import com.wealthfront.magellan.kotlin.Direction.FORWARD
import com.wealthfront.magellan.kotlin.NavigationType.NO_ANIM
import com.wealthfront.magellan.kotlin.Preconditions.Companion.checkArgument
import com.wealthfront.magellan.kotlin.Preconditions.Companion.checkState
import com.wealthfront.magellan.kotlin.Views.Companion.whenMeasured
import com.wealthfront.magellan.kotlin.transitions.DefaultTransition
import com.wealthfront.magellan.kotlin.transitions.Transition
import java.util.*

/**
 * Class responsible for navigating between screens and maintaining collection of screens in a back stack.
 */
class Navigator internal constructor(builder: Builder) : BackHandler {

  private val backStack = ArrayDeque<Screen<*>>()
  private var activity: Activity? = null
  private var menu: Menu? = null
  private var container: ScreenContainer? = null
  private val transition: Transition
  private var overridingTransition: Transition? = null
  private val lifecycleListeners = ArrayList<ScreenLifecycleListener>()
  private var ghostView: View? = null // keep track of the disappearing view we are animating
  private val loggingEnabled: Boolean
  private val eventTracker: EventTracker

  private val isAnimating: Boolean
    get() = ghostView != null

  /**
   * Returns a human-readable string describing the screens in this Navigator's back stack.
   *
   * @return a human-readable description of the back stack.
   */
  val backStackDescription: String
    get() {
      val backStackCopy = ArrayList<Screen<*>>(backStack)
      Collections.reverse(backStackCopy)
      var currentScreen = ""
      if (!backStackCopy.isEmpty()) {
        currentScreen = backStackCopy.removeAt(backStackCopy.size - 1).toString()
      }
      return TextUtils.join(" > ", backStackCopy) + (if (backStackCopy.isEmpty()) "" else " > ") + "[" + currentScreen + "]"
    }

  /**
   * Returns a human-readable string describing the magellan events that happened recently
   * (including the state of the backStack at the time).
   * How many events are kept can be configured using [Builder.maxEventsTracked] (the default is 50).
   *
   * @return a human-readable description of the past events.
   */
  val eventsDescription: String
    get() = eventTracker.getEventsDescription()

  init {
    backStack.push(builder.root)
    transition = builder.transition
    loggingEnabled = builder.loggingEnabled
    eventTracker = EventTracker(builder.maxEventsTracked)
  }

  /**
   * Adds a lifecycle listener that will be notified when each Screen is shown and hidden.
   *
   * @param lifecycleListener  Listener that receives screen lifecycle events
   */
  fun addLifecycleListener(lifecycleListener: ScreenLifecycleListener) {
    lifecycleListeners.add(lifecycleListener)
  }

  /**
   * Unregisters a Screen lifecycle listener. Removed listener will no longer receive screen lifecycle
   * callbacks.
   *
   * @param lifecycleListener  listener to unregister from receiving screen lifecycle events
   */
  fun removeLifecycleListener(lifecycleListener: ScreenLifecycleListener) {
    lifecycleListeners.remove(lifecycleListener)
  }

  /**
   * Initializes the Navigator with Activity instance and Bundle for saved instance state.
   *
   * Call this method from [onCreate][Activity.onCreate] of the Activity associated with this Navigator.
   *
   * @param activity  Activity associated with application
   * @param savedInstanceState  state to restore from previously destroyed activity
   * @throws IllegalStateException when no [ScreenContainer] view present in hierarchy with view id of container
   */
  fun onCreate(activity: Activity, savedInstanceState: Bundle) {
    this.activity = activity
    container = activity.findViewById(R.id.magellan_container) as ScreenContainer
    checkState(container != null, "There must be a ScreenContainer whose id is R.id.magellan_container in the view hierarchy")
    for (screen in backStack) {
      screen.restore(savedInstanceState)
      screen.onRestore(savedInstanceState)
    }
    showCurrentScreen(FORWARD)
  }

  /**
   * Notifies all screens to save instance state in input Bundle.
   *
   * Call this method from [onSaveInstanceState][Activity.onSaveInstanceState] in the Activity associated
   * with this Navigator
   *
   * @param outState  Bundle in which to store screen state information
   */
  fun onSaveInstanceState(outState: Bundle) {
    for (screen in backStack) {
      screen.save(outState)
      screen.onSave(outState)
    }
  }

  /**
   * Attaches options menu to Navigator to allow Screens to control which items are shown/hidden.
   *
   * All menu items are hidden by default.
   *
   * Call this method from [onCreateOptionsMenu][Activity.onCreateOptionsMenu] in the Activity associated
   * with this Navigator
   *
   * @param menu  options menu to be controlled by current screen
   */
  fun onCreateOptionsMenu(menu: Menu) {
    this.menu = menu
    updateMenu()
  }

  /**
   * Updates options menu to Navigator to allow Screens to update which items are shown/hidden.
   *
   * All menu items are hidden by default.
   *
   * Call this method from [onPrepareOptionsMenu][Activity.onPrepareOptionsMenu] in the Activity associated
   * with this Navigator
   *
   * @param menu  options menu to be updated by current screen
   */
  fun onPrepareOptionsMenu(menu: Menu) {
    this.menu = menu
    updateMenu()
  }

  /**
   * Notifies Navigator that the activity's onResume lifecycle callback has been hit. Call this method from
   * `onResume` of the Activity associated with this Navigator.
   *
   * This method will notify the current screen of the lifecycle event if the activity parameter is the same as the
   * activity provided to this Navigator in [onCreate][.onCreate].
   *
   * @param activity  activity that received onResume callback
   */
  fun onResume(activity: Activity) {
    if (sameActivity(activity)) {
      currentScreen().onResume(activity)
    }
  }

  /**
   * Notifies Navigator that the activity's onPause lifecycle callback has been hit. Call this method from
   * [onPause][Activity.onPause] of the Activity associated with this Navigator.
   *
   * This method will notify the current screen of the lifecycle event if the activity parameter is the same as the
   * activity provided to this Navigator in [onCreate][.onCreate].
   *
   * @param activity  activity that received onPause callback
   */
  fun onPause(activity: Activity) {
    if (sameActivity(activity)) {
      currentScreen().onPause(activity)
    }
  }

  /**
   * Notifies Navigator that the activity's onDestroy lifecycle callback has been hit. Call this method from
   * [onDestroy][Activity.onDestroy] of the Activity associated with this Navigator.
   *
   * This method will hid the current screen, and clear references to this Navigators associated activity, menu, and
   * container view, if the activity parameter is the same as the activity provided to this Navigator in
   * [onCreate][.onCreate].
   *
   * @param activity  activity that received onDestroy callback
   */
  fun onDestroy(activity: Activity) {
    if (sameActivity(activity)) {
      hideCurrentScreen()
      this.activity = null
      container = null
      menu = null
    }
  }

  /**
   * Handles back button press by user. Notifies current screen on back button press and allows screen to handle
   * back press itself. If the current screen does not handle back press, and this Navigator has more than one screen,
   * this Navigator will remove the current screen from its collection of screens and display the previous screen.
   * If this Navigator only has one Screen when it receives a call to [.handleBack] it will not handle the back
   * button press.
   *
   * Call this method from within [Activity.onBackPressed] of the activity associated with this Navigator.
   *
   * (Example usage) In the Activity class associated with this Navigator, put:
   * <pre> `
   * @Override
   * public void onBackPressed() {
   * if (!navigator.handleBack()) {
   * super.onBackPressed();
   * }
   * }
  ` *  </pre>
   *
   * @return true if the Navigator consumed the back button click
   */
  override fun handleBack(): Boolean {
    val currentScreen = currentScreen()
    if (currentScreen.handleBack()) {
      return true
    } else {
      if (!atRoot()) {
        goBack()
        return true
      } else {
        return false
      }
    }
  }

  /**
   * Gets the screen on top of this Navigator's back stack.
   *
   * @return the current screen in the back stack
   */
  fun currentScreen(): Screen<*> {
    checkBackStackNotEmpty()
    return backStack.peek()
  }

  /**
   * Returns true if this Navigator is at its root screen, false otherwise.
   *
   * @return true if this Navigator is at its root screen, false otherwise.
   */
  fun atRoot(): Boolean {
    return backStack.size == 1
  }

  /**
   * Clears the back stack of this Navigator and adds the input Screen as the new root of the back stack.
   *
   * @param activity  activity used to verify this Navigator is in an acceptable state when resetWithRoot is called
   * @param root  new root screen for this Navigator
   * @throws IllegalStateException if [.onCreate] has already been called on this Navigator
   */
  fun resetWithRoot(activity: Activity, root: Screen<*>) {
    checkOnCreateNotYetCalled(activity, "resetWithRoot() must be called before onCreate()")
    backStack.clear()
    backStack.push(root)
  }

  /**
   * Change the elements of the back stack according to the implementation of the HistoryRewriter parameter.
   * **Note, this method cannot be called after calling [.onCreate] on this Navigator.** The
   * primary use case for this method is to change the back stack before the navigator is fully initialized (e.g.
   * showing a login screen if necessary). It is possible to manipulate the back stack with a [HistoryRewriter],
   * [.navigate].
   *
   * @param activity  activity used to verify this Navigator is in an acceptable state when resetWithRoot is called
   * @param historyRewriter  rewrites back stack to desired state
   * @throws IllegalStateException if [.onCreate] has already been called on this Navigator
   */
  fun rewriteHistory(activity: Activity, historyRewriter: HistoryRewriter) {
    checkOnCreateNotYetCalled(activity, "rewriteHistory() must be called before onCreate()")
    historyRewriter.rewriteHistory(backStack)
  }

  /**
   * Navigates to the screen on top of the back stack after the HistoryRewriter has rewritten the back stack history.
   *
   * Animates the magellan according to the NavigationType parameter.
   *
   * @param historyRewriter  manipulates the back stack during magellan
   * @param navType  controls how the new screen is displayed to the user
   */
  @JvmOverloads
  fun navigate(historyRewriter: HistoryRewriter, navType: NavigationType = NO_ANIM) {
    navigate(FORWARD, navType, Runnable { historyRewriter.rewriteHistory(backStack) })
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
  fun navigate(historyRewriter: HistoryRewriter, navType: NavigationType, direction: Direction) {
    navigate(direction, navType, Runnable { historyRewriter.rewriteHistory(backStack) })
  }

  /**
   * Replaces screen on top of the back stack with the new screen. When magellan completes, previous screen on top of
   * the back stack will have been removed, and the Screen parameter will be the new top screen on the back stack.
   *
   * @param screen  new top screen on back stack
   */
  fun replace(screen: Screen<*>) {
    replace(screen, NavigationType.GO)
  }

  /**
   * Replaces screen on top of the back stack with the new screen without animation. When magellan completes,
   * previous screen on top of the back stack will have been removed, and the Screen parameter will be the new top
   * screen on the back stack.
   *
   * @param screen  new top screen on back stack
   */
  fun replaceNow(screen: Screen<*>) {
    replace(screen, NO_ANIM)
  }

  /**
   * Shows new screen by animating screen to slide up from bottom of container to cover previous screen. When magellan
   * completes, the Screen parameter will be on top of this Navigator's back stack.
   *
   * If the current screen is already being shown, as determined by `currentScreen().equals(screen)`, this method
   * will do nothing.
   *
   * @param screen  new top screen on back stack
   */
  fun show(screen: Screen<*>) {
    show(screen, NavigationType.SHOW)
  }

  /**
   * Shows new screen without animating the magellan event. When magellan completes, the Screen parameter will be
   * on top of this Navigator's back stack.
   *
   * If the current screen is already being shown, this method will do nothing.
   *
   * @param screen  new top screen on back stack
   */
  fun showNow(screen: Screen<*>) {
    show(screen, NO_ANIM)
  }

  private fun show(screen: Screen<*>, navType: NavigationType) {
    if (!isCurrentScreen(screen)) {
      navigateTo(screen, navType)
    }
  }

  /**
   * Navigates back to previous screen if Screen parameter is currently the top screen on this Navigator's back stack.
   * If the Screen parameter is not the top screen on the back stack, this method does nothing.
   *
   * @param screen  screen to hide
   */
  fun hide(screen: Screen<*>) {
    if (isCurrentScreen(screen)) {
      navigateBack(NavigationType.SHOW)
    }
  }

  /**
   * Returns true if the Screen parameter is the current screen on top of this Navigator's back stack, false otherwise.
   * Equality is determined using `Screen#equals(Object)`.
   *
   * @param screen screen to check if it is currently on top of this Navigator's back stack
   * @return true if the Screen parameter is the top screen on this Navigator's back stack
   */
  fun isCurrentScreen(screen: Screen<*>): Boolean {
    return !backStack.isEmpty() && currentScreen() == screen
  }

  /**
   * Navigates to the Screen parameter. Animates current top screen on this Navigator's back stack out to the left and
   * slides the Screen parameter screen in from the right. At the end of the magellan event, the Screen parameter will
   * be the top screen on this Navigator's back stack.
   *
   * @param screen  new top screen for this Navigator's back stack
   */
  fun goTo(screen: Screen<*>) {
    navigateTo(screen, NavigationType.GO)
  }

  /**
   * Navigates from current screen to previous screen in this Navigator's back stack. Current screen animates out of the
   * view by sliding out to the right and the next screen in the back stack slides in from the left.
   *
   * @throws IllegalStateException if this Navigator only has one screen in its back stack
   */
  fun goBack() {
    checkState(!atRoot(), "Can't go back, this is the last screen. Did you mean to call handleBack() instead?")
    navigateBack(NavigationType.GO)
  }

  /**
   * Navigates from current screen all the way to the root screen in this Navigator's back stack, removing all
   * intermediate screen in this Navigator's back stack along the way. The current screen animates out of the view
   * according to the animation specified by the NavigationType parameter.
   *
   * @param magellanType  determines how the magellan event is animated
   */
  fun goBackToRoot(magellanType: NavigationType) {
    navigate(object : HistoryRewriter {
      override fun rewriteHistory(history: Deque<Screen<*>>) {
        while (history.size > 1) {
          history.pop()
        }
      }
    }, magellanType, Direction.BACKWARD)
  }

  /**
   * Navigates from the current screen back to the Screen parameter wherever it is in this Navigator's back stack.
   * Screens in between the current screen and the Screen parameter on the back stack are removed. If the Screen
   * parameter is not present in this Navigator's back stack, this method is equivalent to
   * [goBackToRoot(NavigationType.GO)][.goBackToRoot]
   *
   * @param screen  screen to navigate back to through this Navigator's back stack
   */
  fun goBackTo(screen: Screen<*>) {
    navigate(object : HistoryRewriter {
      override fun rewriteHistory(history: Deque<Screen<*>>) {
        checkArgument(history.contains(screen), "Can't go back to a screen that isn't in history.")
        while (history.size > 1) {
          if (history.peek() === screen) {
            break
          }
          history.pop()
        }
      }
    }, NavigationType.GO, Direction.BACKWARD)
  }

  /**
   * Navigates from the current screen back to the screen in this Navigator's back stack immediately before the
   * Screen parameter. Screens in between the current screen and the Screen parameter on the back stack are removed.
   * If the Screen parameter is not present in this Navigator's back stack, this method is equivalent to
   * [goBackToRoot(NavigationType)][.goBackToRoot]
   *
   * @param screen  screen to navigate back to through this Navigator's back stack
   * @param magellanType  determines how the magellan event is animated
   */
  @JvmOverloads
  fun goBackBefore(screen: Screen<*>, magellanType: NavigationType = NavigationType.GO) {
    navigate(object : HistoryRewriter {
      override fun rewriteHistory(history: Deque<Screen<*>>) {
        checkArgument(history.contains(screen), "Can't go back past a screen that isn't in history.")
        while (history.size > 1) {
          if (history.pop() === screen) {
            break
          }
        }
      }
    }, magellanType, Direction.BACKWARD)
  }

  private fun replace(screen: Screen<*>, navType: NavigationType) {
    navigate(FORWARD, navType, Runnable {
      backStack.pop()
      backStack.push(screen)
    })
  }

  private fun navigateTo(screen: Screen<*>, navType: NavigationType) {
    navigate(FORWARD, navType, Runnable { backStack.push(screen) })
  }

  private fun navigateBack(navType: NavigationType) {
    navigate(Direction.BACKWARD, navType, Runnable { backStack.pop() })
  }

  private fun navigate(direction: Direction, navType: NavigationType, backStackOperation: Runnable) {
    container!!.setInterceptTouchEvents(true)
    checkNotNull(activity, "The activity cannot be null. Did you forget to call onCreate()?")
    currentScreen().onPause(activity)
    val from = hideCurrentScreen()
    backStackOperation.run()
    val to = showCurrentScreen(direction)
    currentScreen().onResume(activity)
    animateAndRemove(from, to, navType, direction)
    reportEvent(navType, direction)
  }

  private fun animateAndRemove(
      from: View, to: View, navType: NavigationType, direction: Direction) {
    ghostView = from
    val transitionToUse = if (overridingTransition != null) overridingTransition else transition
    overridingTransition = null
    whenMeasured(to, object : Views.OnMeasured {
      override fun onMeasured() {
        transitionToUse.animate(from, to, navType, direction, object : Transition.Callback {
          override fun onAnimationEnd() {
            if (container != null) {
              container!!.removeView(from)
              if (from === ghostView) {
                // Only clear the ghost if it's the same as the view we just removed
                ghostView = null
              }
              container!!.setInterceptTouchEvents(false)
            }
          }
        })
      }
    })
  }

  private fun showCurrentScreen(direction: Direction): View {
    val currentScreen = currentScreen()
    val view = currentScreen.recreateView(activity, this)
    container!!.addView(view, if (direction == FORWARD) container!!.childCount else 0)
    currentScreen.createDialog()
    activity!!.title = currentScreen.getTitle(activity)
    currentScreen.onShow(activity)
    for (lifecycleListener in lifecycleListeners) {
      lifecycleListener.onShow(currentScreen)
    }
    callOnNavigate(currentScreen)
    // Need to post to avoid animation bug on disappearing menu
    Handler(getMainLooper()).post(Runnable { updateMenu() })
    return view
  }

  private fun hideCurrentScreen(): View {
    // if we were already animating a view, just skip it and remove the view immediately
    if (isAnimating) {
      container!!.removeView(ghostView)
      ghostView = null
    }
    checkState(container!!.childCount == 1, "The container view must have a single child, but it had " + container!!.childCount)

    val currentScreen = currentScreen()
    for (lifecycleListener in lifecycleListeners) {
      lifecycleListener.onHide(currentScreen)
    }
    currentScreen.onHide(activity)
    currentScreen.destroyDialog()
    currentScreen.destroyView()
    return container!!.getChildAt(0)
  }

  private fun callOnNavigate(currentScreen: Screen<*>) {
    if (activity is NavigationListener) {
      (activity as NavigationListener).onNavigate(
          ActionBarConfig.with()
              .visible(currentScreen.shouldShowActionBar())
              .animated(currentScreen.shouldAnimateActionBar())
              .colorRes(currentScreen.getActionBarColorRes())
              .build())
    }
  }

  private fun updateMenu() {
    if (menu != null) {
      for (i in 0 until menu!!.size()) {
        menu!!.getItem(i).isVisible = false
      }
      currentScreen().onUpdateMenu(menu)
    }
  }

  private fun sameActivity(activity: Activity): Boolean {
    return this.activity === activity
  }

  private fun checkOnCreateNotYetCalled(activity: Activity, reason: String) {
    checkState(this.activity == null || !sameActivity(activity), reason)
  }

  private fun checkBackStackNotEmpty() {
    checkState(!backStack.isEmpty(), "There must be a least one screen in the backstack")
  }

  private fun reportEvent(navType: NavigationType, direction: Direction) {
    val event = Event(navType, direction, backStackDescription)
    eventTracker.reportEvent(event)
    if (loggingEnabled) {
      Log.d(Navigator::class.java.simpleName, event.toString())
    }
  }

  /**
   * Sets a specific transition to use during the next magellan event. The overriding transition will only be used
   * once, subsequent magellan events will use the default transition specified during construction of this Navigator.
   *
   * @param overridingTransition  transition to override default
   * @return this Navigator that will use the Transition param for its next magellan event
   */
  fun overrideTransition(overridingTransition: Transition): Navigator {
    this.overridingTransition = overridingTransition
    return this
  }

  /**
   * Builder for constructing Navigators with particular parameters.
   *
   * Use [.withRoot] to create a Builder, which can then be used to construct a navigator.
   */
  class Builder internal constructor(private val root: Screen<*>) {
    private var transition: Transition = DefaultTransition()
    private var loggingEnabled: Boolean = false
    private var maxEventsTracked = 50

    fun transition(transition: Transition): Builder {
      this.transition = transition
      return this
    }

    fun loggingEnabled(loggingEnabled: Boolean): Builder {
      this.loggingEnabled = loggingEnabled
      return this
    }

    fun maxEventsTracked(maxEventsTracked: Int): Builder {
      this.maxEventsTracked = maxEventsTracked
      return this
    }

    fun build(): Navigator {
      return Navigator(this)
    }

  }

  companion object {

    fun withRoot(root: Screen<*>): Builder {
      return Builder(root)
    }
  }

}
