package com.wealthfront.magellan.coroutines

import android.app.Application
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transition
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class CreatedLifecycleScopeTest {

  private lateinit var createdScope: CreatedLifecycleScope
  private val context = getApplicationContext<Application>()

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

      createdScope.transition(Destroyed, Resumed(context))

      assertThat(async.isCancelled).isTrue()

      createdScope.transition(Resumed(context), Created(context))

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Not created yet")
    }
  }

  @Test
  fun cancelAfterCreated() {
    runBlockingTest {
      createdScope.transition(Destroyed, Created(context))

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Created(context), Resumed(context))

      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Resumed(context), Created(context))

      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Created(context), Destroyed)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }

  @Test
  fun cancelAfterShown() {
    runBlockingTest {
      createdScope.transition(Destroyed, Shown(context))

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Shown(context), Resumed(context))

      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Resumed(context), Created(context))

      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Created(context), Destroyed)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }

  @Test
  fun cancelAfterResumed() {
    runBlockingTest {
      createdScope.transition(Destroyed, Resumed(context))

      val async = createdScope.async { delay(5000) }
      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Resumed(context), Created(context))

      assertThat(async.isCancelled).isFalse()

      createdScope.transition(Created(context), Destroyed)

      assertThat(async.isCancelled).isTrue()
      assertThat(async.getCancellationException()).hasMessageThat().contains("Destroyed")
    }
  }
}
