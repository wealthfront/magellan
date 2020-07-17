package com.wealthfront.magellan

import android.view.InflateException
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.common.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ScreenContainerTest {

  @Test
  fun addViewInXml() {
    try {
      LayoutInflater.from(application).inflate(R.layout.invalid_screen_container, null)
    } catch (e: InflateException) {
      assertThat(e.message).contains(SCREENCONTAINER_EXCEPTION)
    }
  }

  @Test
  fun viewsAddedProgrammatically() {
    val screenContainer = ScreenContainer(application)
    val firstView = FrameLayout(application)
    val secondView = FrameLayout(application)

    screenContainer.addView(firstView, true)
    assertThat(screenContainer.childCount).isEqualTo(1)

    screenContainer.addView(secondView)
    assertThat(screenContainer.childCount).isEqualTo(2)
  }

  @Test
  fun viewsAddedWithIndexProgrammatically() {
    val screenContainer = ScreenContainer(application)
    val firstView = FrameLayout(application)
    val secondView = FrameLayout(application)

    screenContainer.addView(firstView, 0, true)
    assertThat(screenContainer.childCount).isEqualTo(1)

    screenContainer.addView(secondView)
    assertThat(screenContainer.childCount).isEqualTo(2)
  }
}
