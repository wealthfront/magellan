package com.ryanmoelter.magellanx.core.lifecycle

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
import org.mockito.MockitoAnnotations

internal class LifecycleStateTest {

  private lateinit var mockSession: AutoCloseable

  @Before
  fun setUp() {
    mockSession = MockitoAnnotations.openMocks(this)
  }

  @After
  fun tearDown() {
    mockSession.close()
  }

  @Test
  fun getTheDirectionIShouldGoToGetTo() {
    Destroyed.getDirectionForMovement(Destroyed) shouldBe NO_MOVEMENT
    Destroyed.getDirectionForMovement(Created) shouldBe FORWARD
    Destroyed.getDirectionForMovement(Shown) shouldBe FORWARD
    Destroyed.getDirectionForMovement(Resumed) shouldBe FORWARD

    Created.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Created.getDirectionForMovement(Created) shouldBe NO_MOVEMENT
    Created.getDirectionForMovement(Shown) shouldBe FORWARD
    Created.getDirectionForMovement(Resumed) shouldBe FORWARD

    Shown.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Shown.getDirectionForMovement(Created) shouldBe BACKWARDS
    Shown.getDirectionForMovement(Shown) shouldBe NO_MOVEMENT
    Shown.getDirectionForMovement(Resumed) shouldBe FORWARD

    Resumed.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Created) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Shown) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Resumed) shouldBe NO_MOVEMENT
  }
}
