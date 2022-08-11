package com.wealthfront.magellan.transitions

import android.os.Looper.getMainLooper
import android.view.View
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DefaultTransitionTest {

  private var onAnimationEndCalled = false

  @Before
  fun setUp() {
    onAnimationEndCalled = false
  }

  @Test
  fun animateGoTo() {
    animate(FORWARD)
    shadowOf(getMainLooper()).idle()
    assertThat(onAnimationEndCalled).isTrue()
  }

  @Test
  fun animateGoBack() {
    animate(BACKWARD)
    shadowOf(getMainLooper()).idle()
    assertThat(onAnimationEndCalled).isTrue()
  }

  @Test
  fun interrupt() {
    val transition = animate(FORWARD)
    transition.interrupt()
    assertThat(onAnimationEndCalled).isTrue()
  }

  private fun animate(direction: Direction): MagellanTransition {
    val from = View(getApplicationContext())
    val to = View(getApplicationContext())
    return DefaultTransition().apply {
      animate(from, to, direction) {
        onAnimationEndCalled = true
      }
    }
  }
}
