package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations.openMocks

internal class LifecycleTransitionExtensionsTest {

  @Mock
  lateinit var lifecycleAware: LifecycleAware

  private lateinit var inOrder: InOrder

  private lateinit var mockSession: AutoCloseable

  @Before
  fun setUp() {
    mockSession = openMocks(this)

    inOrder = inOrder(lifecycleAware)
  }

  @After
  fun tearDown() {
    mockSession.close()
  }

  @Test
  fun transitionBetweenLifecycleStates_createToCreated() {
    lifecycleAware.transition(Created, Created)

    verifyNoInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToShown() {
    lifecycleAware.transition(Created, Shown)

    inOrder.verify(lifecycleAware).show()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToResumed() {
    lifecycleAware.transition(Created, Resumed)

    inOrder.verify(lifecycleAware).show()
    inOrder.verify(lifecycleAware).resume()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToDestroy() {
    lifecycleAware.transition(Created, Destroyed)

    inOrder.verify(lifecycleAware).destroy()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToCreated() {
    lifecycleAware.transition(Shown, Created)

    inOrder.verify(lifecycleAware).hide()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToShown() {
    lifecycleAware.transition(Shown, Shown)

    verifyNoInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleAware.transition(Shown, Resumed)

    inOrder.verify(lifecycleAware).resume()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroy() {
    lifecycleAware.transition(Shown, Destroyed)

    inOrder.verify(lifecycleAware).hide()
    inOrder.verify(lifecycleAware).destroy()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleAware.transition(Resumed, Created)

    inOrder.verify(lifecycleAware).pause()
    inOrder.verify(lifecycleAware).hide()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleAware.transition(Resumed, Shown)

    inOrder.verify(lifecycleAware).pause()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToResumed() {
    lifecycleAware.transition(Resumed, Resumed)

    verifyNoInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToDestroy() {
    lifecycleAware.transition(Resumed, Destroyed)

    inOrder.verify(lifecycleAware).pause()
    inOrder.verify(lifecycleAware).hide()
    inOrder.verify(lifecycleAware).destroy()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToCreated() {
    lifecycleAware.transition(Destroyed, Created)

    inOrder.verify(lifecycleAware).create()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToShown() {
    lifecycleAware.transition(Destroyed, Shown)

    inOrder.verify(lifecycleAware).create()
    inOrder.verify(lifecycleAware).show()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleAware.transition(Destroyed, Resumed)

    inOrder.verify(lifecycleAware).create()
    inOrder.verify(lifecycleAware).show()
    inOrder.verify(lifecycleAware).resume()
    verifyNoMoreInteractions(lifecycleAware)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroy() {
    lifecycleAware.transition(Destroyed, Destroyed)

    verifyNoInteractions(lifecycleAware)
  }
}
