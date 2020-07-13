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

class LifecycleStateTest {

  @Mock lateinit var context: Context
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
    assertThat(destroyed.getTheDirectionIShouldGoToGetTo(destroyed)).isEqualTo(NO_MOVEMENT)
    assertThat(destroyed.getTheDirectionIShouldGoToGetTo(created)).isEqualTo(FORWARD)
    assertThat(destroyed.getTheDirectionIShouldGoToGetTo(shown)).isEqualTo(FORWARD)
    assertThat(destroyed.getTheDirectionIShouldGoToGetTo(resumed)).isEqualTo(FORWARD)

    assertThat(created.getTheDirectionIShouldGoToGetTo(destroyed)).isEqualTo(BACKWARDS)
    assertThat(created.getTheDirectionIShouldGoToGetTo(created)).isEqualTo(NO_MOVEMENT)
    assertThat(created.getTheDirectionIShouldGoToGetTo(shown)).isEqualTo(FORWARD)
    assertThat(created.getTheDirectionIShouldGoToGetTo(resumed)).isEqualTo(FORWARD)

    assertThat(shown.getTheDirectionIShouldGoToGetTo(destroyed)).isEqualTo(BACKWARDS)
    assertThat(shown.getTheDirectionIShouldGoToGetTo(created)).isEqualTo(BACKWARDS)
    assertThat(shown.getTheDirectionIShouldGoToGetTo(shown)).isEqualTo(NO_MOVEMENT)
    assertThat(shown.getTheDirectionIShouldGoToGetTo(resumed)).isEqualTo(FORWARD)

    assertThat(resumed.getTheDirectionIShouldGoToGetTo(destroyed)).isEqualTo(BACKWARDS)
    assertThat(resumed.getTheDirectionIShouldGoToGetTo(created)).isEqualTo(BACKWARDS)
    assertThat(resumed.getTheDirectionIShouldGoToGetTo(shown)).isEqualTo(BACKWARDS)
    assertThat(resumed.getTheDirectionIShouldGoToGetTo(resumed)).isEqualTo(NO_MOVEMENT)
  }
}