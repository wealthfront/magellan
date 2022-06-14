package com.wealthfront.magellan

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX
import com.wealthfront.magellan.core.Displayable

@RestrictTo(LIBRARY_GROUP_PREFIX)
public class TransitionFinishedHelper {

  private var isTransitioning = false
  private val transitionFinishedListeners = ArrayDeque<() -> Unit>(1)

  public fun transitionStarted() {
    isTransitioning = true
    transitionFinishedListeners.clear()
  }

  public fun transitionFinished() {
    isTransitioning = false
    while (transitionFinishedListeners.size > 0) {
      transitionFinishedListeners.removeFirst().invoke()
    }
  }

  /**
   * Adds a listener to be called when the navigation transition into this
   * [Displayable] is finished, or immediately if the transition is already finished.
   * @param listener The listener to be called when the transition is finished or immediately.
   */
  public fun whenTransitionFinished(listener: () -> Unit) {
    if (isTransitioning) {
      transitionFinishedListeners.add(listener)
    } else {
      listener.invoke()
    }
  }
}
