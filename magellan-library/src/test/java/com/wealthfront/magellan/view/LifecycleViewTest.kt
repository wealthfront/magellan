package com.wealthfront.magellan.view

import android.content.Context
import android.widget.FrameLayout
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.ShownFieldFromContext
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

public class LifecycleViewTest {

  private lateinit var lifecycleView: ShownFieldFromContext<FrameLayout>

  @Mock private lateinit var frameLayout: FrameLayout
  @Mock private lateinit var context: Context

  @Before
  public fun setUp() {
    initMocks(this)
    lifecycleView = ShownFieldFromContext { frameLayout }
  }

  @Test
  public fun wholeLifecycle() {
    lifecycleView.create(context)
    assertThat(lifecycleView.field).isEqualTo(null)

    lifecycleView.show(context)
    assertThat(lifecycleView.field).isEqualTo(frameLayout)

    lifecycleView.resume(context)
    assertThat(lifecycleView.field).isEqualTo(frameLayout)

    lifecycleView.pause(context)
    assertThat(lifecycleView.field).isEqualTo(frameLayout)

    lifecycleView.hide(context)
    assertThat(lifecycleView.field).isEqualTo(null)

    lifecycleView.destroy(context)
    assertThat(lifecycleView.field).isEqualTo(null)
  }

  @Test
  public fun onCreateView() {
    lifecycleView.show(context)
    assertThat(lifecycleView.field).isEqualTo(frameLayout)
  }

  @Test
  public fun onDestroyView() {
    lifecycleView.hide(context)
    assertThat(lifecycleView.field).isEqualTo(null)
  }
}
