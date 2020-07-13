package com.wealthfront.magellan.core

import android.content.Context
import android.view.View
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.LifecycleStateMachine
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.view.whenMeasured
import java.util.Deque
import java.util.LinkedList

class Navigator(private val container: (Context) -> ScreenContainer?) : LifecycleAwareComponent() {

  private val backStack: Deque<Navigable> = LinkedList()
  private var ghostView: View? = null
  private var containerView: ScreenContainer? = null

  val context: Context?
    get() = currentState.context

  private val lifecycleStateMachine = LifecycleStateMachine()

  private fun currentNavigable(): Navigable? {
    return backStack.peek()
  }

  override fun onCreate(context: Context) {
    currentNavigable()?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Destroyed, Created(context))
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backStack, Destroyed, Created(context))
  }

  override fun onShow(context: Context) {
    containerView = container(context)
    currentNavigable()?.let {
      containerView!!.addView(it.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    currentNavigable()?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Created(context), Destroyed)
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backStack, Created(context), Destroyed)
    containerView = null
  }

  fun goTo(nextScreen: Navigable) {
    navigateTo(nextScreen, GO)

  }

  private fun navigateTo(nextScreen: Navigable, navType: NavigationType) {
    navigate(FORWARD, navType) { backStack ->
      backStack.push(nextScreen)
    }
  }

  private fun navigateBack(navType: NavigationType) {
    navigate(BACKWARD, navType) { backstack ->
      backstack.pop()
    }
  }

  private fun navigate(direction: Direction, navigationType: NavigationType, backstackOperation: (Deque<Navigable>) -> Unit) {
    containerView?.setInterceptTouchEvents(true)
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
          containerView!!.removeView(from)
          if (from == ghostView) {
            // Only clear the ghost if it's the same as the view we just removed
            ghostView = null
          }
          currentNavigable()!!.transitionFinished()
          containerView!!.setInterceptTouchEvents(false)
        }
      }
    }
  }

  private fun showCurrentScreen(direction: Direction): View? {
    val currentScreen = currentNavigable()!!
    currentScreen.transitionStarted()
    attachToLifecycle(currentScreen, detachedState = when (direction) {
      FORWARD -> Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    when (currentState) {
      is Shown, is Resumed -> {
        val indexToRemove = if (direction != FORWARD) {
          0
        } else {
          containerView!!.childCount
        }
        containerView!!.addView(currentScreen.view!!, indexToRemove)
      }
      is Destroyed, is Created -> { }
    }
    return currentScreen.view
  }

  private fun hideCurrentScreen(direction: Direction): View? {
    return currentNavigable()?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> Destroyed
      })
      currentView
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable()?.backPressed() ?: false || goBack()

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