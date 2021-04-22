package com.wealthfront.magellan.lifecycle

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LifecycleRegistryTest {

  private val lifecycleRegistry = LifecycleRegistry()
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @Mock lateinit var lifecycleAware1: LifecycleAware
  @Mock lateinit var lifecycleAware2: LifecycleAware
  @Mock lateinit var lifecycleAware3: LifecycleAware
  @Mock lateinit var lifecycleAware4: LifecycleAware
  @Mock lateinit var lifecycleAware5: LifecycleAware

  @Before
  fun setUp() {
    initMocks(this)
  }

  @Test(expected = IllegalStateException::class)
  fun throwsSinceWeAttachToLifecycleWhenAlreadyAttached() {
    lifecycleRegistry.attachToLifecycle(lifecycleAware1, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware2, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware3, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware5, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware4, Created(context))
    lifecycleRegistry.attachToLifecycle(lifecycleAware5, Created(context))

    assertThat(lifecycleRegistry.listeners).containsExactlyElementsIn(
      setOf(
        lifecycleAware1,
        lifecycleAware2,
        lifecycleAware3,
        lifecycleAware5,
        lifecycleAware4
      )
    ).inOrder()
  }
}
