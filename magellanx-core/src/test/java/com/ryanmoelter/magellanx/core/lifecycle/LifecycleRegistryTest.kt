package com.ryanmoelter.magellanx.core.lifecycle

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LifecycleRegistryTest {

  private val lifecycleRegistry = LifecycleRegistry()
  private val context = ApplicationProvider.getApplicationContext<Context>()

  private lateinit var dummyLifecycleComponent1: DummyLifecycleComponent
  private lateinit var dummyLifecycleComponent2: DummyLifecycleComponent

  @Mock
  lateinit var lifecycleAware1: LifecycleAware

  @Mock
  lateinit var lifecycleAware2: LifecycleAware

  @Mock
  lateinit var lifecycleAware3: LifecycleAware

  @Mock
  lateinit var lifecycleAware4: LifecycleAware

  @Mock
  lateinit var lifecycleAware5: LifecycleAware

  private lateinit var mockSession: AutoCloseable

  @Before
  fun setUp() {
    mockSession = openMocks(this)
    dummyLifecycleComponent1 = DummyLifecycleComponent()
    dummyLifecycleComponent2 = DummyLifecycleComponent()
  }

  @After
  fun tearDown() {
    mockSession.close()
  }

  @Test(expected = IllegalStateException::class)
  fun throwsSinceWeAttachToLifecycleWhenAlreadyAttached() {
    lifecycleRegistry.attachToLifecycle(lifecycleAware1, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware2, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware3, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware5, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware4, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware5, Created(context))

    lifecycleRegistry.listeners shouldContainExactly
      setOf(
        lifecycleAware1,
        lifecycleAware2,
        lifecycleAware3,
        lifecycleAware5,
        lifecycleAware4
      )
  }

  @Test
  fun attachToLifecycleWithMaxState() {
    lifecycleRegistry.create(context)
    lifecycleRegistry.show(context)
    lifecycleRegistry.resume(context)

    lifecycleRegistry.attachToLifecycle(dummyLifecycleComponent1)
    lifecycleRegistry.attachToLifecycleWithMaxState(
      dummyLifecycleComponent2,
      LifecycleLimit.CREATED
    )

    dummyLifecycleComponent1.currentState shouldBe Resumed(context)
    dummyLifecycleComponent2.currentState shouldBe Created(context)
  }

  @Test
  fun attachToLifecycleWithMaxState_notLimited() {
    lifecycleRegistry.create(context)

    lifecycleRegistry.attachToLifecycle(dummyLifecycleComponent1)
    lifecycleRegistry.attachToLifecycleWithMaxState(dummyLifecycleComponent2, LifecycleLimit.SHOWN)

    dummyLifecycleComponent1.currentState shouldBe Created(context)
    dummyLifecycleComponent2.currentState shouldBe Created(context)
  }

  @Test
  fun updateMaxStateForChild_beforeEvents() {
    lifecycleRegistry.create(context)

    lifecycleRegistry.attachToLifecycleWithMaxState(
      dummyLifecycleComponent1,
      LifecycleLimit.CREATED
    )

    dummyLifecycleComponent1.currentState shouldBe Created(context)

    lifecycleRegistry.updateMaxState(dummyLifecycleComponent1, LifecycleLimit.SHOWN)

    dummyLifecycleComponent1.currentState shouldBe Created(context)

    lifecycleRegistry.show(context)
    lifecycleRegistry.resume(context)

    dummyLifecycleComponent1.currentState shouldBe Shown(context)
  }

  @Test
  fun updateMaxStateForChild_afterEvents() {
    lifecycleRegistry.create(context)
    lifecycleRegistry.show(context)
    lifecycleRegistry.resume(context)

    lifecycleRegistry.attachToLifecycleWithMaxState(
      dummyLifecycleComponent1,
      LifecycleLimit.CREATED
    )

    dummyLifecycleComponent1.currentState shouldBe Created(context)

    lifecycleRegistry.updateMaxState(dummyLifecycleComponent1, LifecycleLimit.SHOWN)

    dummyLifecycleComponent1.currentState shouldBe Shown(context)

    lifecycleRegistry.updateMaxState(dummyLifecycleComponent1, LifecycleLimit.CREATED)

    dummyLifecycleComponent1.currentState shouldBe Created(context)
  }

  @Test
  fun onBackPressed_limited() {
    lifecycleRegistry.create(context)
    lifecycleRegistry.show(context)
    lifecycleRegistry.resume(context)

    var unwantedBackPressed = false
    var wantedBackPressed = false

    lifecycleRegistry.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        unwantedBackPressed = true
        true
      },
      LifecycleLimit.CREATED
    )
    lifecycleRegistry.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        wantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )

    val backPressedHandled = lifecycleRegistry.backPressed()

    backPressedHandled.shouldBeTrue()
    wantedBackPressed.shouldBeTrue()
    unwantedBackPressed.shouldBeFalse()
  }

  @Test
  fun onBackPressed_notLimited() {
    lifecycleRegistry.create(context)
    lifecycleRegistry.show(context)
    lifecycleRegistry.resume(context)

    var wantedBackPressed = false
    var unwantedBackPressed = false

    lifecycleRegistry.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        wantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )
    lifecycleRegistry.attachToLifecycleWithMaxState(
      DummyLifecycleComponent {
        unwantedBackPressed = true
        true
      },
      LifecycleLimit.NO_LIMIT
    )

    val backPressedHandled = lifecycleRegistry.backPressed()

    backPressedHandled.shouldBeTrue()
    wantedBackPressed.shouldBeTrue()
    unwantedBackPressed.shouldBeFalse()
  }
}

private class DummyLifecycleComponent(
  val backPressedAction: () -> Boolean = { true }
) : LifecycleAwareComponent() {

  override fun onBackPressed(): Boolean = backPressedAction()
}
