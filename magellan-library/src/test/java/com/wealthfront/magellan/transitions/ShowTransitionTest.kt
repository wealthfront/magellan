package com.wealthfront.magellan.transitions

import android.view.View
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.wealthfront.blend.mock.ImmediateBlend
import com.wealthfront.magellan.Direction
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ShowTransitionTest {

  private lateinit var transition: ShowTransition
  private val blend = ImmediateBlend()
  private var onAnimationEndCalled = false

  @Before
  fun setUp() {
    transition = ShowTransition(blend)
    onAnimationEndCalled = false
  }

  @Test
  fun animateGoTo() {
    val parent = FrameLayout(ApplicationProvider.getApplicationContext())
    val from = View(ApplicationProvider.getApplicationContext())
    val to = View(ApplicationProvider.getApplicationContext())
    parent.addView(from)
    parent.addView(to)

    transition.animate(
      from = from,
      to = to,
      direction = Direction.FORWARD,
      onAnimationEndCallback = { onAnimationEndCalled = true }
    )

    Truth.assertThat(to.scaleX).isWithin(0.01f).of(1f)
    Truth.assertThat(to.scaleY).isWithin(0.01f).of(1f)
    Truth.assertThat(to.translationY).isWithin(0.01f).of(0f)
    Truth.assertThat(to.alpha).isWithin(0.01f).of(1f)
    Truth.assertThat(onAnimationEndCalled).isTrue()
  }

  @Test
  fun animateGoBack() {
    val parent = FrameLayout(ApplicationProvider.getApplicationContext())
    val from = View(ApplicationProvider.getApplicationContext())
    val to = View(ApplicationProvider.getApplicationContext())
    parent.addView(from)
    parent.addView(to)

    transition.animate(
      from = from,
      to = to,
      direction = Direction.BACKWARD,
      onAnimationEndCallback = { onAnimationEndCalled = true }
    )

    Truth.assertThat(to.scaleX).isWithin(0.01f).of(1f)
    Truth.assertThat(to.scaleY).isWithin(0.01f).of(1f)
    Truth.assertThat(to.translationY).isWithin(0.01f).of(0f)
    Truth.assertThat(to.alpha).isWithin(0.01f).of(1f)
    Truth.assertThat(onAnimationEndCalled).isTrue()
  }
}
