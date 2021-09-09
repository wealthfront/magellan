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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.verifyZeroInteractions
import org.mockito.MockitoAnnotations.initMocks

internal class LifecycleStateMachineTest {

  private val lifecycleStateMachine = LifecycleStateMachine()

  @Mock lateinit var lifecycleAware: LifecycleAware
  @Mock lateinit var applicationContext: Context
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
    created = Created(applicationContext)
    shown = Shown(context)
    resumed = Resumed(context)

    `when`(context.applicationContext).thenReturn(applicationContext)
    `when`(applicationContext.applicationContext).thenReturn(applicationContext)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToCreated() {
    lifecycleStateMachine.transition(lifecycleAware, created, created)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToShown() {
    lifecycleStateMachine.transition(lifecycleAware, created, shown)

    inOrder.verify(lifecycleAware).start(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToResumed() {
    lifecycleStateMachine.transition(lifecycleAware, created, resumed)

    inOrder.verify(lifecycleAware).start(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToDestroy() {
    lifecycleStateMachine.transition(lifecycleAware, created, destroyed)

    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToCreated() {
    lifecycleStateMachine.transition(lifecycleAware, shown, created)

    inOrder.verify(lifecycleAware).stop(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToShown() {
    lifecycleStateMachine.transition(lifecycleAware, shown, shown)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleStateMachine.transition(lifecycleAware, shown, resumed)

    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroy() {
    lifecycleStateMachine.transition(lifecycleAware, shown, destroyed)

    inOrder.verify(lifecycleAware).stop(context)
    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleStateMachine.transition(lifecycleAware, resumed, created)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).stop(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleStateMachine.transition(lifecycleAware, resumed, shown)

    inOrder.verify(lifecycleAware).pause(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToResumed() {
    lifecycleStateMachine.transition(lifecycleAware, resumed, resumed)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToDestroy() {
    lifecycleStateMachine.transition(lifecycleAware, resumed, destroyed)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).stop(context)
    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToCreated() {
    lifecycleStateMachine.transition(lifecycleAware, destroyed, created)

    inOrder.verify(lifecycleAware).create(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToShown() {
    lifecycleStateMachine.transition(lifecycleAware, destroyed, shown)

    inOrder.verify(lifecycleAware).create(applicationContext)
    inOrder.verify(lifecycleAware).start(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleStateMachine.transition(lifecycleAware, destroyed, resumed)

    inOrder.verify(lifecycleAware).create(applicationContext)
    inOrder.verify(lifecycleAware).start(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroy() {
    lifecycleStateMachine.transition(lifecycleAware, destroyed, destroyed)

    verifyZeroInteractions(lifecycleAware)
  }
}
