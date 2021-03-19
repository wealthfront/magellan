package com.wealthfront.magellan

import android.app.Activity
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.CurrentNavigableProvider
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.navigation.Navigator
import com.wealthfront.magellan.transitions.MagellanTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition
import rewriteHistoryWithNavigationEvents
import java.util.Deque

public class Navigator internal constructor(
  container: () -> ScreenContainer,
) : Navigator, LifecycleAwareComponent() {

  private val delegate by lifecycle(NavigationDelegate(container))

  override val backStack: List<NavigationEvent>
    get() = delegate.backStack.toList()

  internal var currentNavigableProvider: CurrentNavigableProvider? = null

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

  public fun addLifecycleListener(screenLifecycleListener: ScreenLifecycleListener) {
    attachToLifecycle(screenLifecycleListener)
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

  @JvmOverloads
  public fun replace(navigable: NavigableCompat, magellanTransition: MagellanTransition? = null) {
    delegate.replace(navigable, magellanTransition)
  }

  public fun replaceNow(navigable: NavigableCompat) {
    delegate.replace(navigable, NoAnimationTransition())
  }

  @JvmOverloads
  public fun show(navigable: NavigableCompat, magellanTransition: MagellanTransition? = null) {
    delegate.goTo(navigable, magellanTransition ?: ShowTransition())
  }

  public fun showNow(navigable: NavigableCompat) {
    delegate.goTo(navigable, NoAnimationTransition())
  }

  @JvmOverloads
  public fun goTo(navigable: NavigableCompat, magellanTransition: MagellanTransition? = null) {
    delegate.goTo(navigable, magellanTransition)
  }

  public fun hide(navigable: NavigableCompat) {
    if (currentNavigableProvider!!.isCurrentNavigable(navigable)) {
      goBack()
    }
  }

  public fun goBackToRoot() {
    navigate(BACKWARD) { history ->
      var navigable: NavigableCompat? = null
      while (history.size > 1) {
        navigable = history.pop().navigable
      }
      NavigationEvent(navigable!!, getDefaultTransition())
    }
  }

  public fun atRoot(): Boolean = delegate.atRoot()

  public override fun goBack(): Boolean = delegate.goBack()

  public fun isCurrentScreen(other: NavigableCompat): Boolean = currentNavigableProvider!!.isCurrentNavigable(other)

  public fun currentScreen(): NavigableCompat = currentNavigableProvider!!.navigable!!

  public fun rewriteHistory(activity: Activity?, historyRewriter: HistoryRewriter) {
    checkNotNull(activity != null) { "Activity cannot be null" }
    navigate(historyRewriter)
  }

  @JvmOverloads
  public fun navigate(historyRewriter: HistoryRewriter, magellanTransition: MagellanTransition? = null) {
    navigate(FORWARD) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, magellanTransition)
      backStack.peek()!!
    }
  }

  public fun navigate(historyRewriter: HistoryRewriter, navType: NavigationType) {
    navigate(FORWARD) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, null, navType)
      backStack.peek()!!
    }
  }

  public fun goBackTo(navigable: NavigableCompat) {
    delegate.goBackTo(navigable)
  }

  public fun resetWithRoot(navigable: NavigableCompat) {
    navigate(FORWARD) { backStack ->
      backStack.clear()
      backStack.push(NavigationEvent(navigable, getDefaultTransition()))
      backStack.peek()!!
    }
  }

  public fun navigate(
    historyRewriter: HistoryRewriter,
    magellanTransition: MagellanTransition,
    direction: Direction,
  ) {
    navigate(direction) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, magellanTransition)
      backStack.peek()!!
    }
  }

  public fun navigate(
    historyRewriter: HistoryRewriter,
    navType: NavigationType,
    direction: Direction,
  ) {
    navigate(direction) { backStack ->
      historyRewriter.rewriteHistoryWithNavigationEvents(backStack, null, navType)
      backStack.peek()!!
    }
  }
}
