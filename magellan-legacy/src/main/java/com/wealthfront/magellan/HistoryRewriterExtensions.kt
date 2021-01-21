import com.wealthfront.magellan.HistoryRewriter
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.toTransition
import com.wealthfront.magellan.transitions.DefaultTransition
import java.util.ArrayDeque
import java.util.Deque

internal fun HistoryRewriter.rewriteHistoryWithNavigationEvents(
  oldBackStack: Deque<NavigationEvent>,
  navigationType: NavigationType? = null
) {
  val modifiedBackStackOfScreens = ArrayDeque(oldBackStack.map { it.navigable })
  rewriteHistory(modifiedBackStackOfScreens)
  oldBackStack.clear()
  modifiedBackStackOfScreens.forEach { screen ->
    val navEvent = oldBackStack.find { it.navigable == screen }
    if (navEvent != null) {
      oldBackStack.add(navEvent)
    } else {
      oldBackStack.add(NavigationEvent(screen, DefaultTransition()))
    }
  }
  if (navigationType != null) {
    val lastNav = oldBackStack.last
    oldBackStack.removeLast()
    oldBackStack.add(NavigationEvent(lastNav.navigable, navigationType.toTransition()))
  }
}
