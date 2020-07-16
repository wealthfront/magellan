package com.wealthfront.magellan.view

import android.content.Context
import android.widget.FrameLayout
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class LifecycleViewTest {

  private lateinit var lifecycleView: LifecycleWithContext<FrameLayout>

  @Mock lateinit var frameLayout: FrameLayout
  @Mock lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    lifecycleView = LifecycleWithContext { frameLayout }
  }

  @Test
  fun wholeLifecycle() {
    lifecycleView.create(context)
    assertThat(lifecycleView.data).isEqualTo(null)

    lifecycleView.show(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.resume(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.pause(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.hide(context)
    assertThat(lifecycleView.data).isEqualTo(null)

    lifecycleView.destroy(context)
    assertThat(lifecycleView.data).isEqualTo(null)
  }

  @Test
  fun onCreateView() {
    lifecycleView.show(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)
  }

  @Test
  fun onDestroyView() {
    lifecycleView.hide(context)
    assertThat(lifecycleView.data).isEqualTo(null)
  }
}
