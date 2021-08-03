package com.example.magellan.compose.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.magellan.compose.Content
import com.example.magellan.compose.WhenShown
import com.example.magellan.compose.transitions.MagellanComposeTransition
import com.example.magellan.compose.transitions.defaultTransition
import com.example.magellan.compose.transitions.noTransition
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleLimit
import com.wealthfront.magellan.lifecycle.LifecycleLimiter
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationPropagator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalAnimationApi::class)
public class ComposeNavigator : LifecycleAwareComponent(), Displayable<@Composable () -> Unit> {

  private val navigationPropagator = NavigationPropagator

  private val lifecycleLimiter by lifecycle(LifecycleLimiter())

  private val backStackFlow = MutableStateFlow<List<ComposeNavigationEvent>>(emptyList())
  public val backStack: List<ComposeNavigationEvent> = backStackFlow.value
  private val currentNavigable = backStack.lastOrNull()?.navigable

  private val currentNavigationEventFlow = backStackFlow.map { it.lastOrNull() }

  // TODO: make default transition configurable
  private val transitionFlow: MutableStateFlow<MagellanComposeTransition> =
    MutableStateFlow(defaultTransition)
  private val directionFlow: MutableStateFlow<Direction> = MutableStateFlow(FORWARD)

  override val view: (@Composable () -> Unit)
    get() = { Content() }

  @Composable
  private fun Content() {
    WhenShown {
      val currentNavigable by currentNavigationEventFlow
        .map { it?.navigable }
        .collectAsState(null)
      val currentTransitionSpec by transitionFlow.collectAsState()
      val currentDirection by directionFlow.collectAsState()

      AnimatedContent(
        targetState = currentNavigable,
        transitionSpec = currentTransitionSpec.getTransitionForDirection(currentDirection)
      ) { navigable ->
        navigable?.Content()
      }
    }
  }

  override fun onDestroy(context: Context) {
    backStack
      .map { it.navigable }
      .forEach { lifecycleLimiter.removeFromLifecycle(it) }
    backStackFlow.value = emptyList()
  }

  public fun goTo(
    navigable: Navigable<@Composable () -> Unit>,
    overrideTransitionSpec: MagellanComposeTransition? = null
  ) {
    navigate(FORWARD) { backStack ->
      backStack + ComposeNavigationEvent(
        navigable = navigable,
        transitionSpec = overrideTransitionSpec ?: defaultTransition
      )
    }
  }

  public fun replace(
    navigable: Navigable<@Composable () -> Unit>,
    overrideTransitionSpec: MagellanComposeTransition? = null
  ) {
    navigate(FORWARD) { backStack ->
      backStack - backStack.last() + ComposeNavigationEvent(
        navigable = navigable,
        transitionSpec = overrideTransitionSpec ?: defaultTransition
      )
    }
  }

  public fun goBack(): Boolean {
    return if (!atRoot()) {
      navigate(BACKWARD) { backStack ->
        backStack - backStack.last()
      }
      true
    } else {
      false
    }
  }

  public fun goBackTo(navigable: Navigable<@Composable () -> Unit>) {
    navigate(BACKWARD) { backStack ->
      val mutableBackstack = backStack.toMutableList()
      while (navigable !== mutableBackstack.last().navigable) {
        mutableBackstack.removeLast()
      }
      mutableBackstack.toList()
    }
  }

  public fun resetWithRoot(navigable: Navigable<@Composable () -> Unit>) {
    navigate(FORWARD) {
      listOf(ComposeNavigationEvent(navigable, noTransition))
    }
  }

  public fun navigate(
    direction: Direction,
    backStackOperation: (backStack: List<ComposeNavigationEvent>) -> List<ComposeNavigationEvent>
  ) {
    // TODO: Intercept touch events, if necessary
    navigationPropagator.beforeNavigation()
    val from = backStack.lastOrNull()?.navigable
    val oldBackStack = backStack
    val newBackStack = backStackOperation(backStack)
    directionFlow.value = direction
    transitionFlow.value = when (direction) {
      FORWARD -> newBackStack.last().transitionSpec
      BACKWARD -> oldBackStack.last().transitionSpec
    }
    findBackstackChangesAndUpdateStates(
      oldBackStack = oldBackStack.map { it.navigable },
      newBackStack = newBackStack.map { it.navigable }
    )
    updateCurrentBackstackLifecycleLimits(newBackStack.map { it.navigable }, from)
    navigationPropagator.afterNavigation()
  }

  private fun findBackstackChangesAndUpdateStates(
    oldBackStack: List<NavigableCompat<*>>,
    newBackStack: List<NavigableCompat<*>>
  ) {
    val oldNavigables = oldBackStack.toSet()
    val newNavigables = newBackStack.toSet()

    (oldNavigables - newNavigables).forEach { oldNavigable ->
      lifecycleLimiter.removeFromLifecycle(oldNavigable)
    }

    (newNavigables - oldNavigables).forEach { newNavigable ->
      lifecycleLimiter.attachToLifecycleWithMaxState(newNavigable, LifecycleLimit.CREATED)
    }
  }

  private fun updateCurrentBackstackLifecycleLimits(
    newBackStack: List<NavigableCompat<@Composable () -> Unit>>,
    from: NavigableCompat<@Composable () -> Unit>?
  ) {
    newBackStack.forEachIndexed { index, navigableCompat ->
      lifecycleLimiter.updateMaxStateForChild(
        navigableCompat,
        if (index != newBackStack.lastIndex) {
          LifecycleLimit.CREATED
        } else {
          from?.let { navigationPropagator.onNavigatedFrom(from) }
          LifecycleLimit.NO_LIMIT
        }
      )
    }

    navigationPropagator.onNavigatedTo(newBackStack.last())
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  public fun atRoot(): Boolean = backStack.size <= 1
}

@ExperimentalAnimationApi
public data class ComposeNavigationEvent(
  val navigable: Navigable<@Composable () -> Unit>,
  val transitionSpec: MagellanComposeTransition
)
