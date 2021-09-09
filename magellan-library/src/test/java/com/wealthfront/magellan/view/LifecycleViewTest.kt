package com.wealthfront.magellan.view

import android.content.Context
import android.widget.FrameLayout
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.ShownFieldFromContext
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class LifecycleViewTest {

  private lateinit var lifecycleView: ShownFieldFromContext<FrameLayout>

  @Mock lateinit var frameLayout: FrameLayout
  @Mock lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    lifecycleView = ShownFieldFromContext { frameLayout }
  }

  @Test
  fun wholeLifecycle() {
    lifecycleView.create(context)
    assertThat(lifecycleView.data).isEqualTo(null)

    lifecycleView.start(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.resume(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.pause(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)

    lifecycleView.stop(context)
    assertThat(lifecycleView.data).isEqualTo(null)

    lifecycleView.destroy(context)
    assertThat(lifecycleView.data).isEqualTo(null)
  }

  @Test
  fun onCreateView() {
    lifecycleView.start(context)
    assertThat(lifecycleView.data).isEqualTo(frameLayout)
  }

  @Test
  fun onDestroyView() {
    lifecycleView.stop(context)
    assertThat(lifecycleView.data).isEqualTo(null)
  }
}
