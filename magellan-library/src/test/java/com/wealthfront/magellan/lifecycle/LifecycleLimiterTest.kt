package com.wealthfront.magellan.lifecycle

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LifecycleLimiterTest {

  lateinit var lifecycleLimiter: LifecycleLimiter
  private lateinit var dummyLifecycleComponent: DummyLifecycleComponent
  private lateinit var dummyLifecycleComponent2: DummyLifecycleComponent
  private lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    context = getApplicationContext()
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

  @Test
  fun onBackPressed_limited() {
    lifecycleLimiter.create(context)
    lifecycleLimiter.show(context)
    lifecycleLimiter.resume(context)

    var unwantedBackPressed = false
    var wantedBackPressed = false

    lifecycleLimiter.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        unwantedBackPressed = true
        true
      },
      LifecycleLimit.CREATED
    )
    lifecycleLimiter.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        wantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )

    val backPressedHandled = lifecycleLimiter.backPressed()

    assertThat(backPressedHandled).isTrue()
    assertThat(wantedBackPressed).isTrue()
    assertThat(unwantedBackPressed).isFalse()
  }

  @Test
  fun onBackPressed_notLimited() {
    lifecycleLimiter.create(context)
    lifecycleLimiter.show(context)
    lifecycleLimiter.resume(context)

    var wantedBackPressed = false
    var unwantedBackPressed = false

    lifecycleLimiter.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        wantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )
    lifecycleLimiter.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        unwantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )

    val backPressedHandled = lifecycleLimiter.backPressed()

    assertThat(backPressedHandled).isTrue()
    assertThat(wantedBackPressed).isTrue()
    assertThat(unwantedBackPressed).isFalse()
  }
}

private class DummyLifecycleComponent(
  val backPressedAction: () -> Boolean = { true }
) : LifecycleAwareComponent() {

  override fun onBackPressed(): Boolean = backPressedAction()
}
