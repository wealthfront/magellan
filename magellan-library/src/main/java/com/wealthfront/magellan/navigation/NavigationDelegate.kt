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
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.childNavigables
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import com.wealthfront.magellan.transitions.Transition
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

  fun goTo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigateTo(nextNavigableCompat, overrideTransition ?: DefaultTransition())
  }

  fun show(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigateTo(nextNavigableCompat, overrideTransition ?: ShowTransition())
  }

  fun replaceAndGo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    replace(nextNavigableCompat, overrideTransition ?: DefaultTransition())
  }

  fun replaceAndShow(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    replace(nextNavigableCompat, overrideTransition ?: ShowTransition())
  }

  private fun replace(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigate(FORWARD) { backStack ->
      backStack.pop()
      backStack.push(NavigationEvent(nextNavigableCompat, overrideTransition ?: ShowTransition()))
    }
  }

  private fun navigateTo(nextNavigableCompat: NavigableCompat, overrideTransition: Transition? = null) {
    navigate(FORWARD) { backStack ->
      backStack.push(NavigationEvent(nextNavigableCompat, overrideTransition ?: DefaultTransition()))
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
    val transition = backStackOperation.invoke(backStack).transition
    val to = showCurrentNavigable(direction)
    animateAndRemove(from, to, direction, transition)
  }

  private fun animateAndRemove(
    from: View?,
    to: View?,
    direction: Direction,
    transition: Transition
  ) {
    currentNavigable!!.transitionStarted()
    to?.whenMeasured {
      transition.animate(from, to, direction, object : Transition.Callback {
        override fun onAnimationEnd() {
          if (context != null) {
            containerView!!.removeView(from)
            currentNavigable!!.transitionFinished()
            containerView!!.setInterceptTouchEvents(false)
          }
        }
      })
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
        containerView!!.addView(
          currentNavigable!!.view!!,
          direction.indexToAddView(containerView!!))
      }
      is LifecycleState.Destroyed, is LifecycleState.Created -> {
      }
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
        rootNavigable.childNavigables()
          .filterIsInstance(ActionBarModifier::class.java)
          .forEach { it.onUpdateMenu(menu) }
        (updateMenuForNavigable as? ActionBarModifier)?.onUpdateMenu(menu)
        updateMenuForNavigable?.childNavigables()
          ?.filterIsInstance(ActionBarModifier::class.java)
          ?.forEach { it.onUpdateMenu(menu) }
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
