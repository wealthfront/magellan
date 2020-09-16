package com.wealthfront.magellan.navigation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.NO_ANIM
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.childNavigables
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.view.ActionBarConfig
import com.wealthfront.magellan.view.ActionBarModifier
import com.wealthfront.magellan.view.whenMeasured
import java.util.Stack

class NavigationDelegate(
  private val rootNavigable: NavigableCompat,
  private val container: () -> ScreenContainer
) : LifecycleAwareComponent() {

  private var containerView: ScreenContainer? = null
  private val navigationPropagator = NavigationPropagator
  private var activity: Activity? = null
  var menu: Menu? = null
    set(value) {
      field = value
      updateMenu(menu)
    }

  val backStack: Stack<NavigationEvent> = Stack()
  var currentNavigableSetup: ((NavigableCompat) -> Unit)? = null

  private val currentNavigable: NavigableCompat?
    get() {
      return if (backStack.isNotEmpty()) {
        backStack.peek()?.navigable
      } else {
        null
      }
    }

  private val context: Context?
    get() = currentState.context

  override fun onCreate(context: Context) {
    activity = context as Activity
  }

  override fun onShow(context: Context) {
    containerView = container()
    currentNavigable?.let {
      containerView!!.addView(it.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    backStack.navigables().forEach {
      removeFromLifecycle(it, detachedState = LifecycleState.Destroyed)
    }
    backStack.clear()
    containerView = null
    menu = null
    activity = null
  }

  fun goTo(nextNavigableCompat: NavigableCompat) {
    navigateTo(nextNavigableCompat, GO)
  }

  fun show(nextNavigableCompat: NavigableCompat) {
    navigateTo(nextNavigableCompat, SHOW)
  }

  fun replaceAndGo(nextNavigableCompat: NavigableCompat) {
    replace(nextNavigableCompat, GO)
  }

  fun replaceAndShow(nextNavigableCompat: NavigableCompat) {
    replace(nextNavigableCompat, SHOW)
  }

  private fun replace(nextNavigableCompat: NavigableCompat, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(NavigationEvent(nextNavigableCompat, navType))
    }
  }

  fun replaceNow(nextNavigableCompat: NavigableCompat) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(NavigationEvent(nextNavigableCompat, NO_ANIM))
    }
  }

  fun hide(navigableCompat: NavigableCompat) {
    if (backStack.peek().navigable == navigableCompat) {
      goBack()
    }
  }

  private fun navigateTo(nextNavigableCompat: NavigableCompat, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      backStack.push(NavigationEvent(nextNavigableCompat, navType))
    }
  }

  private fun navigateBack() {
    navigate(BACKWARD) { backStack ->
      backStack.pop()
    }
  }

  fun navigate(
    direction: Direction,
    backStackOperation: (Stack<NavigationEvent>) -> NavigationEvent
  ) {
    containerView?.setInterceptTouchEvents(true)
    val from = hideCurrentNavigable(direction)
    val navType = backStackOperation.invoke(backStack).navigationType
    val to = showCurrentNavigable(direction)
    animateAndRemove(from, to, direction, navType)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    navType: NavigationType
  ) {
    val transition = DefaultTransition()
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      transition.animate(from, to, navType, direction) {
        if (context != null) {
          containerView!!.removeView(from)
          currentNavigable!!.transitionFinished()
          containerView!!.setInterceptTouchEvents(false)
        }
      }
    }
  }

  private fun showCurrentNavigable(direction: Direction): View? {
    currentNavigableSetup?.invoke(currentNavigable!!)
    attachToLifecycle(
      currentNavigable!!, detachedState = when (direction) {
      FORWARD -> LifecycleState.Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    setupCurrentScreenToBeShown(currentNavigable!!)
    navigationPropagator.onNavigate()
    navigationPropagator.showCurrentNavigable(currentNavigable!!)
    callOnNavigate(currentNavigable!!)
    when (currentState) {
      is LifecycleState.Shown, is LifecycleState.Resumed -> {
        containerView!!.addView(currentNavigable!!.view!!)
      }
      is LifecycleState.Destroyed, is LifecycleState.Created -> { }
    }
    return currentNavigable!!.view
  }

  private fun hideCurrentNavigable(direction: Direction): View? {
    return currentNavigable?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(
        currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> LifecycleState.Destroyed
      })
      navigationPropagator.hideCurrentNavigable(currentNavigable)
      currentView
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  fun goBack(): Boolean {
    return if (!atRoot()) {
      navigateBack()
      true
    } else {
      false
    }
  }

  private fun atRoot() = backStack.size <= 1

  private fun setupCurrentScreenToBeShown(currentNavigable: NavigableCompat) {
    if (currentNavigable is Journey<*>) {
      menu?.let { currentNavigable.setMenu(it) }
    }
    currentNavigable.setTitle(currentNavigable.getTitle(activity!!))
    updateMenu(menu, currentNavigable)
  }

  private fun updateMenu(menu: Menu?, navItem: NavigableCompat? = null) {
    // Need to post to avoid animation bug on disappearing menu
    val updateMenuForNavigable = navItem ?: currentNavigable
    Handler(Looper.getMainLooper()).post {
      menu?.let {
        for (i in 0 until menu.size()) {
          menu.getItem(i).isVisible = false
        }
        (rootNavigable as? ActionBarModifier)?.onUpdateMenu(menu)
        rootNavigable.childNavigables().filterIsInstance(ActionBarModifier::class.java).filter { it.shouldShowChildNavigablesMenu() }.forEach { it.onUpdateMenu(menu) }
        (updateMenuForNavigable as? ActionBarModifier)?.onUpdateMenu(menu)
        updateMenuForNavigable?.childNavigables()?.filterIsInstance(ActionBarModifier::class.java)?.filter { it.shouldShowChildNavigablesMenu() }?.forEach { it.onUpdateMenu(menu) }
      }
    }
  }

  private fun callOnNavigate(navItem: NavigableCompat) {
    if (navItem is ActionBarModifier) {
      (activity as? ActionBarConfigListener)?.onNavigate(
        ActionBarConfig.with()
          .visible(navItem.shouldShowActionBar())
          .animated(navItem.shouldAnimateActionBar())
          .colorRes(navItem.actionBarColorRes)
          .build())
    }
  }
}
