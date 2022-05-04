package com.ryanmoelter.magellanx.core.lifecycle

import android.content.Context
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.BACKWARDS
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.FORWARD
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.NO_MOVEMENT
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LifecycleStateTest {

  @Mock
  private lateinit var context: Context
  private lateinit var destroyed: Destroyed
  private lateinit var created: Created
  private lateinit var shown: Shown
  private lateinit var resumed: Resumed

  private lateinit var mockSession: AutoCloseable

  @Before
  fun setUp() {
    mockSession = MockitoAnnotations.openMocks(this)
    destroyed = Destroyed
    created = Created(context)
    shown = Shown(context)
    resumed = Resumed(context)
  }

  @After
  fun tearDown() {
    mockSession.close()
  }

  @Test
  fun getEarlierOfCurrentState() {
    destroyed.getEarlierOfCurrentState().shouldBeInstanceOf<Destroyed>()
    created.getEarlierOfCurrentState().shouldBeInstanceOf<Created>()
    shown.getEarlierOfCurrentState().shouldBeInstanceOf<Created>()
    resumed.getEarlierOfCurrentState().shouldBeInstanceOf<Created>()
  }

  @Test
  fun getTheDirectionIShouldGoToGetTo() {
    destroyed.getDirectionForMovement(destroyed) shouldBe NO_MOVEMENT
    destroyed.getDirectionForMovement(created) shouldBe FORWARD
    destroyed.getDirectionForMovement(shown) shouldBe FORWARD
    destroyed.getDirectionForMovement(resumed) shouldBe FORWARD

    created.getDirectionForMovement(destroyed) shouldBe BACKWARDS
    created.getDirectionForMovement(created) shouldBe NO_MOVEMENT
    created.getDirectionForMovement(shown) shouldBe FORWARD
    created.getDirectionForMovement(resumed) shouldBe FORWARD

    shown.getDirectionForMovement(destroyed) shouldBe BACKWARDS
    shown.getDirectionForMovement(created) shouldBe BACKWARDS
    shown.getDirectionForMovement(shown) shouldBe NO_MOVEMENT
    shown.getDirectionForMovement(resumed) shouldBe FORWARD

    resumed.getDirectionForMovement(destroyed) shouldBe BACKWARDS
    resumed.getDirectionForMovement(created) shouldBe BACKWARDS
    resumed.getDirectionForMovement(shown) shouldBe BACKWARDS
    resumed.getDirectionForMovement(resumed) shouldBe NO_MOVEMENT
  }
}
