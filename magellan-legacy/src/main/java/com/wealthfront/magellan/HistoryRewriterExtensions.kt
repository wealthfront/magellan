import android.view.View
import com.wealthfront.magellan.HistoryRewriter
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.toTransition
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.ArrayDeque
import java.util.Deque

internal fun HistoryRewriter.rewriteHistoryWithNavigationEvents(
  oldBackStack: Deque<NavigationEvent<View>>,
  magellanTransition: MagellanTransition? = null,
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
      oldBackStack.add(NavigationEvent(screen, getDefaultTransition()))
    }
  }
  if (magellanTransition != null || navigationType != null) {
    val lastNav = oldBackStack.peek()!!
    oldBackStack.pop()
    oldBackStack.addFirst(NavigationEvent(lastNav.navigable, magellanTransition ?: navigationType!!.toTransition()))
  }
}
