package com.ryanmoelter.magellanx.core.coroutines

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.transition
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
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
  fun cancelBeforeCreated() {
    runTest {
      shownScope.transition(Destroyed, Created(context))

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Not shown yet"

      shownScope.transition(Created(context), Resumed(context))

      async.isCancelled.shouldBeTrue()

      shownScope.transition(Resumed(context), Created(context))

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Not shown yet"
    }
  }

  @Test
  fun cancelAfterShown() {
    runTest {
      shownScope.transition(Destroyed, Shown(context))

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()

      shownScope.transition(Shown(context), Resumed(context))

      async.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed(context), Created(context))

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
    }
  }

  @Test
  fun cancelMultipleAfterShown() {
    runTest {
      shownScope.transition(Destroyed, Shown(context))

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      val async2 = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()
      async2.isCancelled.shouldBeFalse()

      shownScope.transition(Shown(context), Resumed(context))

      async.isCancelled.shouldBeFalse()
      async2.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed(context), Created(context))

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
      async2.isCancelled.shouldBeTrue()
      async2.getCancellationException().message shouldContain "Hidden"
    }
  }

  @Test
  fun cancelAfterResumed() {
    runTest {
      shownScope.transition(Destroyed, Resumed(context))

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed(context), Created(context))

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
    }
  }
}
