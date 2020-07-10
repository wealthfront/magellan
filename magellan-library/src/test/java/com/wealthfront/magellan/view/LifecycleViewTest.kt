package com.wealthfront.magellan.view

import android.content.Context
import android.widget.FrameLayout
import org.junit.Before

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class LifecycleViewTest {

  private lateinit var lifecycleView: LifecycleView<FrameLayout>

  @Mock lateinit var frameLayout: FrameLayout
  @Mock lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    lifecycleView = LifecycleView { frameLayout }
  }

  @Test
  fun wholeLifecycle() {
    lifecycleView.create(context)
    assertThat(lifecycleView.view).isEqualTo(null)

    lifecycleView.show(context)
    assertThat(lifecycleView.view).isEqualTo(frameLayout)

    lifecycleView.resume(context)
    assertThat(lifecycleView.view).isEqualTo(frameLayout)

    lifecycleView.pause(context)
    assertThat(lifecycleView.view).isEqualTo(frameLayout)

    lifecycleView.hide(context)
    assertThat(lifecycleView.view).isEqualTo(null)

    lifecycleView.destroy(context)
    assertThat(lifecycleView.view).isEqualTo(null)
  }

  @Test
  fun onCreateView() {
    lifecycleView.show(context)
    assertThat(lifecycleView.view).isEqualTo(frameLayout)
  }

  @Test
  fun onDestroyView() {
    lifecycleView.hide(context)
    assertThat(lifecycleView.view).isEqualTo(null)
  }

}