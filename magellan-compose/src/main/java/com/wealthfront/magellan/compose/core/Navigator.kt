package com.wealthfront.magellan.compose.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.compose.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.compose.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.compose.lifecycle.LifecycleStateMachine
import com.wealthfront.magellan.compose.view.whenMeasured
import com.wealthfront.magellan.transitions.DefaultTransition
import java.util.Deque
import java.util.LinkedList

class Navigator(private val container: (Context) -> ViewGroup) : LifecycleAwareComponent() {

  private val backStack: Deque<Screen> = LinkedList()
  private var ghostView: View? = null

  val context: Context?
    get() = currentState.context

  private val lifecycleStateMachine = LifecycleStateMachine()

  private fun currentScreen(): Screen? {
    return backStack.peek()
  }

  override fun onCreate(context: Context) {
    currentScreen()?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Destroyed, Created(context))
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backStack, Destroyed, Created(context))
  }

  override fun onShow(context: Context) {
    currentScreen()?.let {
      container(context).addView(it.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    currentScreen()?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Created(context), Destroyed)
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backStack, Created(context), Destroyed)
  }

  fun goTo(nextScreen: Screen) {
    navigateTo(nextScreen, GO)

  }

  private fun navigateTo(nextScreen: Screen, navType: NavigationType) {
    navigate(FORWARD, navType) { backStack ->
      backStack.push(nextScreen)
    }
  }

  private fun navigateBack(navType: NavigationType) {
    navigate(BACKWARD, navType) { backstack ->
      backstack.pop()
    }
  }

  private fun navigate(direction: Direction, navigationType: NavigationType, backstackOperation: (Deque<Screen>) -> Unit) {
    val from = hideCurrentScreen(direction)
    backstackOperation.invoke(backStack)
    val to = showCurrentScreen(direction)
    animateAndRemove(from, to, navigationType, direction)
  }

  private fun animateAndRemove(from: View?, to: View?, navType: NavigationType, direction: Direction) {
    ghostView = from
    val transition = DefaultTransition()
    to?.whenMeasured {
      transition.animate(from, to, navType, direction) {
        if (context != null) {
          container(context!!).removeView(from)
          if (from == ghostView) {
            // Only clear the ghost if it's the same as the view we just removed
            ghostView = null
          }
          currentScreen()!!.transitionFinished()
        }
      }
    }
  }

  private fun showCurrentScreen(direction: Direction): View? {
    val currentScreen = currentScreen()!!
    currentScreen.transitionStarted()
    attachToLifecycle(currentScreen, detachedState = when (direction) {
      FORWARD -> Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    when (currentState) {
      is Shown, is Resumed -> {
        val navigationContainer = container(context!!)
        val indexToRemove = if (direction != FORWARD) {
          0
        } else {
          navigationContainer.childCount
        }
        navigationContainer.addView(currentScreen.view!!, indexToRemove)
      }
      is Destroyed, is Created -> { }
    }
    return currentScreen.view
  }

  private fun hideCurrentScreen(direction: Direction): View? {
    return currentScreen()?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> Destroyed
      })
      currentView
    }
  }

  override fun onBackPressed(): Boolean = currentScreen()?.backPressed() ?: false || goBack()

  fun goBack(): Boolean {
    return if (!atRoot()) {
      navigateBack(GO)
      true
    } else {
      false
    }
  }

  private fun atRoot() = backStack.size == 1

}