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

  private lateinit var shownScope: ShownLifecycleScope
  private val context = Activity()

  @Before
  fun setUp() {
    shownScope = ShownLifecycleScope()
    Dispatchers.setMain(Dispatchers.Unconfined)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun cancelBeforeCreated() {
    runBlockingTest {
      shownScope.create(context)

      val async = shownScope.async { delay(5000) }
      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Not shown yet")

      shownScope.start(context)
      shownScope.resume(context)

      assertThat(async.isCancelled).isTrue()

      shownScope.pause(context)
      shownScope.stop(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Not shown yet")
    }
  }

  @Test
  fun cancelAfterShown() {
    runBlockingTest {
      shownScope.create(context)
      shownScope.start(context)

      val async = shownScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      shownScope.resume(context)

      assertThat(async.isCancelled).isFalse()

      shownScope.pause(context)
      shownScope.stop(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }

  @Test
  fun cancelMultipleAfterShown() {
    runBlockingTest {
      shownScope.create(context)
      shownScope.start(context)

      val async = shownScope.async { delay(5000) }
      val async2 = shownScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()
      assertThat(async2.isCancelled).isFalse()

      shownScope.resume(context)

      assertThat(async.isCancelled).isFalse()
      assertThat(async2.isCancelled).isFalse()

      shownScope.pause(context)
      shownScope.stop(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
      assertThat(async2.isCancelled).isTrue()
      assertThat(async2.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }

  @Test
  fun cancelAfterResumed() {
    runBlockingTest {
      shownScope.create(context)
      shownScope.start(context)
      shownScope.resume(context)

      val async = shownScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      shownScope.pause(context)
      shownScope.stop(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    }
  }
}
