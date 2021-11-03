package com.wealthfront.magellan.internal.test

import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.internal.test.TransitionState.FINISHED
import com.wealthfront.magellan.internal.test.TransitionState.NOT_STARTED
import com.wealthfront.magellan.internal.test.TransitionState.STARTED
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.navigation.Navigator

/**
 * A dummy step that is useful for testing navigation logic, for example when creating a custom
 * [Navigator] implementation. Can query for its current [LifecycleState] with [currentState], and
 * its current [TransitionState] with [currentTransitionState].
 */
public open class DummyStep : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate) {
  /**
   * The current state of this step's transitions. Starts in [NOT_STARTED], moves to [STARTED] in
   * [transitionStarted], and finally to [FINISHED] in [transitionFinished].
   */
  public var currentTransitionState: TransitionState = NOT_STARTED
    private set

  override fun transitionStarted() {
    currentTransitionState = STARTED
  }

  override fun transitionFinished() {
    currentTransitionState = FINISHED
  }
}

/**
 * The current state of this step's transitions. Starts in [NOT_STARTED], moves to [STARTED] in
 * [Displayable.transitionStarted], and finally to [FINISHED] in [Displayable.transitionFinished].
 */
public enum class TransitionState {
  NOT_STARTED, STARTED, FINISHED
}
