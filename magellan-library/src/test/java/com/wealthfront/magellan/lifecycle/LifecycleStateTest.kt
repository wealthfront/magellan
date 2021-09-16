package com.wealthfront.magellan.lifecycle

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.BACKWARDS
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.FORWARD
import com.wealthfront.magellan.lifecycle.LifecycleStateDirection.NO_MOVEMENT
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

internal class LifecycleStateTest {

  @Mock private lateinit var context: Context
  private lateinit var destroyed: Destroyed
  private lateinit var created: Created
  private lateinit var shown: Shown
  private lateinit var resumed: Resumed

  @Before
  fun setUp() {
    initMocks(this)
    destroyed = Destroyed
    created = Created(context)
    shown = Shown(context)
    resumed = Resumed(context)
  }

  @Test
  fun getEarlierOfCurrentState() {
    assertThat(destroyed.getEarlierOfCurrentState()).isInstanceOf(Destroyed::class.java)
    assertThat(created.getEarlierOfCurrentState()).isInstanceOf(Created::class.java)
    assertThat(shown.getEarlierOfCurrentState()).isInstanceOf(Created::class.java)
    assertThat(resumed.getEarlierOfCurrentState()).isInstanceOf(Created::class.java)
  }

  @Test
  fun getTheDirectionIShouldGoToGetTo() {
    assertThat(destroyed.getDirectionForMovement(destroyed)).isEqualTo(NO_MOVEMENT)
    assertThat(destroyed.getDirectionForMovement(created)).isEqualTo(FORWARD)
    assertThat(destroyed.getDirectionForMovement(shown)).isEqualTo(FORWARD)
    assertThat(destroyed.getDirectionForMovement(resumed)).isEqualTo(FORWARD)

    assertThat(created.getDirectionForMovement(destroyed)).isEqualTo(BACKWARDS)
    assertThat(created.getDirectionForMovement(created)).isEqualTo(NO_MOVEMENT)
    assertThat(created.getDirectionForMovement(shown)).isEqualTo(FORWARD)
    assertThat(created.getDirectionForMovement(resumed)).isEqualTo(FORWARD)

    assertThat(shown.getDirectionForMovement(destroyed)).isEqualTo(BACKWARDS)
    assertThat(shown.getDirectionForMovement(created)).isEqualTo(BACKWARDS)
    assertThat(shown.getDirectionForMovement(shown)).isEqualTo(NO_MOVEMENT)
    assertThat(shown.getDirectionForMovement(resumed)).isEqualTo(FORWARD)

    assertThat(resumed.getDirectionForMovement(destroyed)).isEqualTo(BACKWARDS)
    assertThat(resumed.getDirectionForMovement(created)).isEqualTo(BACKWARDS)
    assertThat(resumed.getDirectionForMovement(shown)).isEqualTo(BACKWARDS)
    assertThat(resumed.getDirectionForMovement(resumed)).isEqualTo(NO_MOVEMENT)
  }
}
