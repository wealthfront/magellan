package com.wealthfront.magellan

import android.app.Activity
import android.view.Menu
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition
import rewriteHistoryWithNavigationEvents
import java.util.Deque

public class Navigator internal constructor(
  override val journey: Step<*>,
  container: () -> ScreenContainer
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(journey, container))

  override val backStack: Deque<NavigationEvent>
    get() = delegate.backStack

  internal var menu: Menu? = null
    set(value) {
      field = value
      delegate.menu = value
    }

  init {
    delegate.currentNavigableSetup = { navItem ->
      if (navItem is Screen<*>) {
        navItem.navigator = this
      }
      if (navItem is MultiScreen<*>) {
        navItem.screens.forEach { it.setNavigator(this) }
      }
    }
  }

  public fun navigate(backStackOperation: (Deque<NavigationEvent>) -> NavigationEvent) {
    navigate(FORWARD, backStackOperation)
  }

  public fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> NavigationEvent
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  public fun replace(navigable: NavigableCompat, magellanTransition: MagellanTransition? = null) {
    delegate.replace(navigable, magellanTransition)
  }

  public fun replaceNow(navigable: NavigableCompat) {
    delegate.replace(navigable, NoAnimationTransition())
  }

  public fun show(navigable: NavigableCompat) {
    delegate.goTo(navigable, ShowTransition())
  }

  public fun showNow(navigable: NavigableCompat) {
    delegate.goTo(navigable, NoAnimationTransition())
  }

  public fun goTo(navigable: NavigableCompat) {
    delegate.goTo(navigable)
  }

  public fun hideNow(navigable: NavigableCompat) {
    hide(navigable, NoAnimationTransition())
  }

  @JvmOverloads
  public fun hide(
    navigable: NavigableCompat,
    overrideMagellanTransition: MagellanTransition? = null
  ) {
    navigate(BACKWARD) { backStack ->
      val backStackItem = backStack.find { it.navigable == navigable }
        ?: throw IllegalStateException("Cannot find navigable (${navigable::class.java.simpleName}) in the backstack!")
      backStack.remove(backStackItem)
      NavigationEvent(navigable, overrideMagellanTransition ?: ShowTransition())
    }
  }

  public fun hide(magellanTransition: MagellanTransition? = null) {
    navigate(BACKWARD) { backStack ->
      NavigationEvent(backStack.pop().navigable, magellanTransition ?: ShowTransition())
    }
  }

  public fun goBackToRoot() {
    navigate(Direction.BACKWARD) { history ->
      var navigable: NavigableCompat? = null
      while (history.size > 1) {
        navigable = history.pop().navigable
      }
      NavigationEvent(navigable!!, DefaultTransition())
    }
  }

  public fun goBack(): Boolean {
    return delegate.goBack()
  }

  public fun rewriteHistory(activity: Activity?, historyRewriter: HistoryRewriter) {
    checkNotNull(activity != null) { "Activity cannot be null" }
    navigate(historyRewriter)
  }

  public fun navigate(historyRewriter: HistoryRewriter) {
    navigate(FORWARD) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack)
      backStack.peek()!!
    }
  }

  public fun navigate(historyRewriter: HistoryRewriter, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, navType)
      backStack.peek()!!
    }
  }

  public fun navigate(
    historyRewriter: HistoryRewriter,
    navType: NavigationType,
    direction: Direction
  ) {
    navigate(direction) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, navType)
      backStack.peek()!!
    }
  }
}
