package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

internal class LifecycleLimiterTest {

  lateinit var lifecycleLimiter: LifecycleLimiter
  private lateinit var dummyLifecycleComponent: DummyLifecycleComponent
  private lateinit var dummyLifecycleComponent2: DummyLifecycleComponent
  @Mock internal lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    lifecycleLimiter = LifecycleLimiter()
    dummyLifecycleComponent = DummyLifecycleComponent()
    dummyLifecycleComponent2 = DummyLifecycleComponent()
  }

  @Test
  fun attachToLifecycleWithMaxState() {
    lifecycleLimiter.create(context)
    lifecycleLimiter.show(context)
    lifecycleLimiter.resume(context)

    lifecycleLimiter.attachToLifecycle(dummyLifecycleComponent)
    lifecycleLimiter.attachToLifecycleWithMaxState(dummyLifecycleComponent2, LifecycleLimit.CREATED)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Resumed(context))
    assertThat(dummyLifecycleComponent2.currentState).isEqualTo(LifecycleState.Created(context))
  }

  @Test
  fun attachToLifecycleWithMaxState_notLimited() {
    lifecycleLimiter.create(context)

    lifecycleLimiter.attachToLifecycle(dummyLifecycleComponent)
    lifecycleLimiter.attachToLifecycleWithMaxState(dummyLifecycleComponent2, LifecycleLimit.SHOWN)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Created(context))
    assertThat(dummyLifecycleComponent2.currentState).isEqualTo(LifecycleState.Created(context))
  }

  @Test
  fun updateMaxStateForChild_beforeEvents() {
    lifecycleLimiter.create(context)

    lifecycleLimiter.attachToLifecycleWithMaxState(dummyLifecycleComponent, LifecycleLimit.CREATED)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Created(context))

    lifecycleLimiter.updateMaxStateForChild(dummyLifecycleComponent, LifecycleLimit.SHOWN)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Created(context))

    lifecycleLimiter.show(context)
    lifecycleLimiter.resume(context)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Shown(context))
  }

  @Test
  fun updateMaxStateForChild_afterEvents() {
    lifecycleLimiter.create(context)
    lifecycleLimiter.show(context)
    lifecycleLimiter.resume(context)

    lifecycleLimiter.attachToLifecycleWithMaxState(dummyLifecycleComponent, LifecycleLimit.CREATED)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Created(context))

    lifecycleLimiter.updateMaxStateForChild(dummyLifecycleComponent, LifecycleLimit.SHOWN)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Shown(context))

    lifecycleLimiter.updateMaxStateForChild(dummyLifecycleComponent, LifecycleLimit.CREATED)

    assertThat(dummyLifecycleComponent.currentState).isEqualTo(LifecycleState.Created(context))
  }
}

private class DummyLifecycleComponent : LifecycleAwareComponent()
