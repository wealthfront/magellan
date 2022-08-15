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
class CrossfadeTransitionTest {

  var onAnimationEndCalled = false
  var from = View(getApplicationContext())
  var to = View(getApplicationContext())

  @Before
  fun setUp() {
    onAnimationEndCalled = false
    from.visibility = View.VISIBLE
    to.visibility = View.GONE
  }

  @Test
  fun animateShow() {
    animate(FORWARD)
    shadowOf(getMainLooper()).idle()
    assertThat(onAnimationEndCalled).isTrue()
    assertThat(from.visibility).isEqualTo(View.GONE)
    assertThat(to.visibility).isEqualTo(View.VISIBLE)
  }

  @Test
  fun animateHide() {
    animate(BACKWARD)
    shadowOf(getMainLooper()).idle()
    assertThat(onAnimationEndCalled).isTrue()
    assertThat(from.visibility).isEqualTo(View.GONE)
    assertThat(to.visibility).isEqualTo(View.VISIBLE)
  }

  @Test
  fun interrupt() {
    val transition = animate(FORWARD)
    transition.interrupt()
    assertThat(onAnimationEndCalled).isTrue()
    assertThat(from.visibility).isEqualTo(View.GONE)
    assertThat(to.visibility).isEqualTo(View.VISIBLE)
  }

  private fun animate(direction: Direction): MagellanTransition {
    return CrossfadeTransition().apply {
      animate(from, to, direction) {
        onAnimationEndCalled = true
      }
    }
  }
}
