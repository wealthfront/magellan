package com.wealthfront.magellan.coroutines

import android.app.Activity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
internal class ShownLifecycleScopeTest {

  private lateinit var navScope: ShownLifecycleScope
  private val context = Activity()

  @Before
  fun setUp() {
    navScope = ShownLifecycleScope()
    Dispatchers.setMain(Dispatchers.Unconfined)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun cancelAfterCreated() {
    runBlockingTest {
      navScope.create(context)

      val async = navScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      navScope.show(context)
      navScope.resume(context)

      assertThat(async.isCancelled).isFalse()

      navScope.pause(context)
      navScope.hide(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }

  @Test
  fun cancelAfterShown() {
    runBlockingTest {
      navScope.create(context)
      navScope.show(context)

      val async = navScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      navScope.resume(context)

      assertThat(async.isCancelled).isFalse()

      navScope.pause(context)
      navScope.hide(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }

  @Test
  fun cancelAfterResumed() {
    runBlockingTest {
      navScope.create(context)
      navScope.show(context)
      navScope.resume(context)

      val async = navScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      navScope.pause(context)
      navScope.hide(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }
}
