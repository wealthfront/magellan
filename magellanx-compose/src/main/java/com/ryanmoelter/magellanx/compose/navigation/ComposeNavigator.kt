package com.ryanmoelter.magellanx.compose.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.compose.Content
import com.ryanmoelter.magellanx.compose.WhenShown
import com.ryanmoelter.magellanx.compose.navigation.Direction.BACKWARD
import com.ryanmoelter.magellanx.compose.navigation.Direction.FORWARD
import com.ryanmoelter.magellanx.compose.transitions.MagellanComposeTransition
import com.ryanmoelter.magellanx.compose.transitions.defaultTransition
import com.ryanmoelter.magellanx.compose.transitions.noTransition
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleLimit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalAnimationApi::class)
public class ComposeNavigator : LifecycleAwareComponent(), Displayable<@Composable () -> Unit> {

  /**
   * The backstack. The last item in each list is the top of the stack.
   */
  private val backStackFlow = MutableStateFlow<List<ComposeNavigationEvent>>(emptyList())

  /**
   * Get a snapshot of the current item in [backStackFlow]. The last item is the top of the stack.
   */
  public val backStack: List<ComposeNavigationEvent>
    get() = backStackFlow.value

  /**
   * Get a snapshot of the current navigable, i.e. the last item of the current [backStack].
   */
  private val currentNavigable
    get() = backStack.lastOrNull()?.navigable

  private val currentNavigationEventFlow = backStackFlow.map { it.lastOrNull() }
  private val currentNavigableFlow = currentNavigationEventFlow.map { it?.navigable }

  // TODO: make default transition configurable
  private val transitionFlow: MutableStateFlow<MagellanComposeTransition> =
    MutableStateFlow(defaultTransition)
  private val directionFlow: MutableStateFlow<Direction> = MutableStateFlow(FORWARD)

  override val view: (@Composable () -> Unit)
    get() = {
      WhenShown {
        Content()
      }
    }

  @Composable
  private fun Content() {
    val currentNavigable by currentNavigableFlow.collectAsState(null)
    val currentTransitionSpec by transitionFlow.collectAsState()
    val currentDirection by directionFlow.collectAsState()

    AnimatedContent(
      targetState = currentNavigable,
      transitionSpec = currentTransitionSpec.getTransitionForDirection(currentDirection)
    ) { navigable ->
      DisposableEffect(
        key1 = navigable,
        effect = {
          if (navigable != null) {
            lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.NO_LIMIT)
            onDispose {
              if (children.contains(navigable)) {
                if (backStack.map { it.navigable }.contains(navigable)) {
                  lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.CREATED)
                } else {
                  removeFromLifecycle(navigable)
                }
              }
            }
          } else {
            onDispose { }
          }
        }
      )
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        navigable?.Content()
      }
    }
  }

  override fun onDestroy(context: Context) {
    backStack
      .map { it.navigable }
      .forEach { lifecycleRegistry.removeFromLifecycle(it) }
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
    NavigationPropagator._beforeNavigation.tryEmit(Unit)
    val fromNavigable = currentNavigable
    val oldBackStack = backStack
    val newBackStack = backStackOperation(backStack)
    val toNavigable = newBackStack.last().navigable
    directionFlow.value = direction
    transitionFlow.value = when (direction) {
      FORWARD -> newBackStack.last().transitionSpec
      BACKWARD -> oldBackStack.last().transitionSpec
    }
    findBackstackChangesAndUpdateStates(
      oldBackStack = oldBackStack.map { it.navigable },
      newBackStack = newBackStack.map { it.navigable },
      from = fromNavigable
    )
    /* Optimistically update toNavigable's limit to smooth out transitions. This will also get set
     * by the DisposedEffect in #Content(), but redundancy is fine. fromNavigable's limit will be
     * reset to CREATED in DisposedEffect.
     */
    lifecycleRegistry.updateMaxState(toNavigable, LifecycleLimit.NO_LIMIT)
    NavigationPropagator._onNavigatedTo.tryEmit(toNavigable)
    backStackFlow.value = newBackStack
    NavigationPropagator._afterNavigation.tryEmit(Unit)
  }

  private fun findBackstackChangesAndUpdateStates(
    oldBackStack: List<Navigable<*>>,
    newBackStack: List<Navigable<*>>,
    from: Navigable<*>?
  ) {
    val oldNavigables = oldBackStack.toSet()
    // Don't remove the last Navigable (from) until it's removed from composition in DisposedEffect
    val newNavigables = (newBackStack + from).filterNotNull().toSet()

    (oldNavigables - newNavigables).forEach { oldNavigable ->
      lifecycleRegistry.removeFromLifecycle(oldNavigable)
    }

    (newNavigables - oldNavigables).forEach { newNavigable ->
      lifecycleRegistry.attachToLifecycleWithMaxState(newNavigable, LifecycleLimit.CREATED)
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  public fun atRoot(): Boolean = backStack.size <= 1
}

@ExperimentalAnimationApi
public data class ComposeNavigationEvent(
  val navigable: Navigable<@Composable () -> Unit>,
  val transitionSpec: MagellanComposeTransition
)
