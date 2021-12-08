package com.wealthfront.magellan.transitions

import android.view.View
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.blend.mock.ImmediateBlend
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DefaultTransitionTest {

  private lateinit var transition: DefaultTransition
  private val blend = ImmediateBlend()
  private var onAnimationEndCalled = false

  @Before
  fun setUp() {
    transition = DefaultTransition(blend)
    onAnimationEndCalled = false
  }

  @Test
  fun animateGoTo() {
    val parent = FrameLayout(getApplicationContext())
    val from = View(getApplicationContext())
    val to = View(getApplicationContext())
    parent.addView(from)
    parent.addView(to)

    transition.animate(
      from = from,
      to = to,
      direction = FORWARD,
      onAnimationEndCallback = { onAnimationEndCalled = true }
    )

    assertThat(to.scaleX).isWithin(0.01f).of(1f)
    assertThat(to.scaleY).isWithin(0.01f).of(1f)
    assertThat(to.alpha).isWithin(0.01f).of(1f)
    assertThat(onAnimationEndCalled).isTrue()
  }

  @Test
  fun animateGoBack() {
    val parent = FrameLayout(getApplicationContext())
    val from = View(getApplicationContext())
    val to = View(getApplicationContext())
    parent.addView(from)
    parent.addView(to)

    transition.animate(
      from = from,
      to = to,
      direction = BACKWARD,
      onAnimationEndCallback = { onAnimationEndCalled = true }
    )

    assertThat(to.scaleX).isWithin(0.01f).of(1f)
    assertThat(to.scaleY).isWithin(0.01f).of(1f)
    assertThat(to.alpha).isWithin(0.01f).of(1f)
    assertThat(onAnimationEndCalled).isTrue()
  }
}