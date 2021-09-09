package com.wealthfront.magellan.view

import android.content.Context
import android.widget.FrameLayout
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.CreateAndAttachFieldToLifecycleWhenShownDelegate
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class CreateAndAttachFieldToLifecycleWhenShownDelegateTest {

  private lateinit var lifecycleView: CreateAndAttachFieldToLifecycleWhenShownDelegate<FrameLayout>

  @Mock lateinit var frameLayout: FrameLayout
  @Mock lateinit var context: Context

  @Before
  fun setUp() {
    initMocks(this)
    lifecycleView = CreateAndAttachFieldToLifecycleWhenShownDelegate { frameLayout }
  }

  @Test
  fun wholeLifecycle() {
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
  fun onCreateView() {
    lifecycleView.show(context)
    assertThat(lifecycleView.field).isEqualTo(frameLayout)
  }

  @Test
  fun onDestroyView() {
    lifecycleView.hide(context)
    assertThat(lifecycleView.field).isEqualTo(null)
  }
}
