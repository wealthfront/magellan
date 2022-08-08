package com.wealthfront.magellan.coroutines

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ShownLifecycleScopeTest {

  private lateinit var shownScope: ShownLifecycleScope
  private val context: Context = getApplicationContext<Application>()

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
  fun cancelBeforeCreated() = runTest {
    shownScope.transition(Destroyed, Created(context))

    val async = shownScope.async { delay(5000) }
    assertThat(async.isCancelled).isTrue()
    assertThat(async.getCancellationException()).hasMessageThat().contains("Not shown yet")

    shownScope.transition(Created(context), Resumed(context))

    assertThat(async.isCancelled).isTrue()

    shownScope.transition(Resumed(context), Created(context))

    assertThat(async.isCancelled).isTrue()
    assertThat(async.getCancellationException()).hasMessageThat().contains("Not shown yet")
  }

  @Test
  fun cancelAfterShown() = runTest {
    shownScope.transition(Destroyed, Shown(context))

    val async = shownScope.async { delay(5000) }
    assertThat(async.isCancelled).isFalse()

    shownScope.transition(Shown(context), Resumed(context))

    assertThat(async.isCancelled).isFalse()

    shownScope.transition(Resumed(context), Created(context))

    assertThat(async.isCancelled).isTrue()
    assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
  }

  @Test
  fun cancelMultipleAfterShown() = runTest {
    shownScope.transition(Destroyed, Shown(context))

    val async = shownScope.async { delay(5000) }
    val async2 = shownScope.async { delay(5000) }
    assertThat(async.isCancelled).isFalse()
    assertThat(async2.isCancelled).isFalse()

    shownScope.transition(Shown(context), Resumed(context))

    assertThat(async.isCancelled).isFalse()
    assertThat(async2.isCancelled).isFalse()

    shownScope.transition(Resumed(context), Created(context))

    assertThat(async.isCancelled).isTrue()
    assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
    assertThat(async2.isCancelled).isTrue()
    assertThat(async2.getCancellationException()).hasMessageThat().contains("Hidden")
  }

  @Test
  fun cancelAfterResumed() = runTest {
    shownScope.transition(Destroyed, Resumed(context))

    val async = shownScope.async { delay(5000) }
    assertThat(async.isCancelled).isFalse()

    shownScope.transition(Resumed(context), Created(context))

    assertThat(async.isCancelled).isTrue()
    assertThat(async.getCancellationException()).hasMessageThat().contains("Hidden")
  }
}
