package com.wealthfront.magellan.core

import android.content.Context
import android.view.ViewGroup
import com.wealthfront.magellan.core.NavigationDirection.BACKWARD
import com.wealthfront.magellan.core.NavigationDirection.FORWARD
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleRegistry
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.LifecycleStateMachine
import java.util.Deque
import java.util.LinkedList

class Navigator(private val container: (Context) -> ViewGroup) : LifecycleAwareComponent() {

  private val backstack: Deque<Screen> = LinkedList()

  var currentNavigable: Screen? = null
    private set

  val context: Context?
    get() = currentState.context

  private val lifecycleStateMachine = LifecycleStateMachine()

  override fun onCreate(context: Context) {
    currentNavigable?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Destroyed, Created(context))
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backstack, Destroyed, Created(context))
  }

  override fun onShow(context: Context) {
    currentNavigable?.let {
      container(context).addView(it.view!!)
    }
  }

  override fun onDestroy(context: Context) {
    currentNavigable?.let {
      lifecycleStateMachine.transitionBetweenLifecycleStates(it, Created(context), Destroyed)
    }
    lifecycleStateMachine.transitionBetweenLifecycleStates(backstack, Created(context), Destroyed)
  }

  fun goTo(nextNavigable: Screen, direction: NavigationDirection = FORWARD) {
    val currentView = currentNavigable?.let { currentNavigable ->
      val currentView = currentNavigable.view
      removeFromLifecycle(currentNavigable, detachedState = when (direction) {
        FORWARD -> currentState.getEarlierOfCurrentState()
        BACKWARD -> Destroyed
      })
      currentView
    }
    attachToLifecycle(nextNavigable, detachedState = when (direction) {
      FORWARD -> Destroyed
      BACKWARD -> currentState.getEarlierOfCurrentState()
    })
    if (direction == FORWARD) {
      backstack.add(nextNavigable)
    }
    currentNavigable = nextNavigable
    when (currentState) {
      is Shown, is Resumed -> {
        val navigationContainer = container(context!!)
        navigationContainer.addView(nextNavigable.view!!)
        if (currentView != null) {
          navigationContainer.removeView(currentView)
        }
        Unit
      }
      is Destroyed, is Created -> { }
    }.let { }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  fun goBack(): Boolean {
    return if (canGoBack()) {
      goTo(backstack.pop(), BACKWARD)
      true
    } else {
      false
    }
  }

  private fun canGoBack() = backstack.isNotEmpty()

}

enum class NavigationDirection {
  FORWARD,
  BACKWARD
}