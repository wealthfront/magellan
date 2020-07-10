package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Mockito.verifyNoMoreInteractions

class LifecycleStateMachineTest {

  private val lifecycleStateMachine = LifecycleStateMachine()
  
  @Mock lateinit var lifecycleAware: LifecycleAware
  @Mock lateinit var context: Context

  private lateinit var inOrder: InOrder
  private lateinit var destroyed: Destroyed
  private lateinit var created: Created
  private lateinit var shown: Shown
  private lateinit var resumed: Resumed

  @Before
  fun setUp() {
    initMocks(this)

    inOrder = inOrder(lifecycleAware)
    destroyed = Destroyed
    created = Created(context)
    shown = Shown(context)
    resumed = Resumed(context)

  }

  @Test
  fun transitionBetweenLifecycleStates_createToCreated() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, created, created)

    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToShown() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, created, shown)

    inOrder.verify(lifecycleAware).show(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToResumed() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, created, resumed)

    inOrder.verify(lifecycleAware).show(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToDestroy() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, created, destroyed)

    inOrder.verify(lifecycleAware).destroy(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToCreated() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, shown, created)

    inOrder.verify(lifecycleAware).hide(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToShown() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, shown, shown)

    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, shown, resumed)

    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroy() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, shown, destroyed)

    inOrder.verify(lifecycleAware).hide(context)
    inOrder.verify(lifecycleAware).destroy(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, resumed, created)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).hide(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, resumed, shown)

    inOrder.verify(lifecycleAware).pause(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToResumed() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, resumed, resumed)

    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToDestroy() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, resumed, destroyed)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).hide(context)
    inOrder.verify(lifecycleAware).destroy(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToCreated() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, destroyed, created)

    inOrder.verify(lifecycleAware).create(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToShown() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, destroyed, shown)

    inOrder.verify(lifecycleAware).create(context)
    inOrder.verify(lifecycleAware).show(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, destroyed, resumed)

    inOrder.verify(lifecycleAware).create(context)
    inOrder.verify(lifecycleAware).show(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroy() {
    lifecycleStateMachine.transitionBetweenLifecycleStates(lifecycleAware, destroyed, destroyed)

    verifyNoMoreInteractions(lifecycleAware)
  }
}