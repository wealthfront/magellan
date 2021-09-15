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

internal class LifecycleTransitionExtensionsTest {

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
    lifecycleAware.transition(created, created)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToShown() {
    lifecycleAware.transition(created, shown)

    inOrder.verify(lifecycleAware).show(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToResumed() {
    lifecycleAware.transition(created, resumed)

    inOrder.verify(lifecycleAware).show(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToDestroy() {
    lifecycleAware.transition(created, destroyed)

    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToCreated() {
    lifecycleAware.transition(shown, created)

    inOrder.verify(lifecycleAware).hide(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToShown() {
    lifecycleAware.transition(shown, shown)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleAware.transition(shown, resumed)

    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroy() {
    lifecycleAware.transition(shown, destroyed)

    inOrder.verify(lifecycleAware).hide(context)
    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleAware.transition(resumed, created)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).hide(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleAware.transition(resumed, shown)

    inOrder.verify(lifecycleAware).pause(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToResumed() {
    lifecycleAware.transition(resumed, resumed)

    verifyZeroInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToDestroy() {
    lifecycleAware.transition(resumed, destroyed)

    inOrder.verify(lifecycleAware).pause(context)
    inOrder.verify(lifecycleAware).hide(context)
    inOrder.verify(lifecycleAware).destroy(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToCreated() {
    lifecycleAware.transition(destroyed, created)

    inOrder.verify(lifecycleAware).create(applicationContext)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToShown() {
    lifecycleAware.transition(destroyed, shown)

    inOrder.verify(lifecycleAware).create(applicationContext)
    inOrder.verify(lifecycleAware).show(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleAware.transition(destroyed, resumed)

    inOrder.verify(lifecycleAware).create(applicationContext)
    inOrder.verify(lifecycleAware).show(context)
    inOrder.verify(lifecycleAware).resume(context)
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroy() {
    lifecycleAware.transition(destroyed, destroyed)

    verifyZeroInteractions(lifecycleAware)
  }
}
