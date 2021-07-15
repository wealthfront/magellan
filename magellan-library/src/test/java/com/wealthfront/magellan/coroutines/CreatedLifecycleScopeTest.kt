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
internal class CreatedLifecycleScopeTest {

  private lateinit var createdScope: CreatedLifecycleScope
  private val context = Activity()

  @Before
  fun setUp() {
    createdScope = CreatedLifecycleScope()
    Dispatchers.setMain(Dispatchers.Unconfined)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun cancelBeforeCreated() {
    runBlockingTest {

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Not created yet")

      createdScope.create(context)
      createdScope.show(context)
      createdScope.resume(context)

      assertThat(async.isCancelled).isTrue()

      createdScope.pause(context)
      createdScope.hide(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Not created yet")
    }
  }

  @Test
  fun cancelAfterCreated() {
    runBlockingTest {
      createdScope.create(context)

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.show(context)
      createdScope.resume(context)

      assertThat(async.isCancelled).isFalse()

      createdScope.pause(context)
      createdScope.hide(context)

      assertThat(async.isCancelled).isFalse()

      createdScope.destroy(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }

  @Test
  fun cancelAfterShown() {
    runBlockingTest {
      createdScope.create(context)
      createdScope.show(context)

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.resume(context)

      assertThat(async.isCancelled).isFalse()

      createdScope.pause(context)
      createdScope.hide(context)

      assertThat(async.isCancelled).isFalse()

      createdScope.destroy(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }

  @Test
  fun cancelAfterResumed() {
    runBlockingTest {
      createdScope.create(context)
      createdScope.show(context)
      createdScope.resume(context)

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.pause(context)
      createdScope.hide(context)

      assertThat(async.isCancelled).isFalse()

      createdScope.destroy(context)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }
}
