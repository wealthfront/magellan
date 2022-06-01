package com.ryanmoelter.magellanx.core.coroutines

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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
internal class ShownLifecycleScopeTest {

  private lateinit var shownScope: ShownLifecycleScope

  @Before
  fun setUp() {
    shownScope = ShownLifecycleScope()
  }

  @Test
  fun cancelBeforeCreated() {
    runTest {
      shownScope.transition(Destroyed, Created)

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Not shown yet"

      shownScope.transition(Created, Resumed)

      async.isCancelled.shouldBeTrue()

      shownScope.transition(Resumed, Created)

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Not shown yet"
    }
  }

  @Test
  fun cancelAfterShown() {
    runTest {
      shownScope.transition(Destroyed, Shown)

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()

      shownScope.transition(Shown, Resumed)

      async.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed, Created)

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
    }
  }

  @Test
  fun cancelMultipleAfterShown() {
    runTest {
      shownScope.transition(Destroyed, Shown)

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      val async2 = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()
      async2.isCancelled.shouldBeFalse()

      shownScope.transition(Shown, Resumed)

      async.isCancelled.shouldBeFalse()
      async2.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed, Created)

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
      async2.isCancelled.shouldBeTrue()
      async2.getCancellationException().message shouldContain "Hidden"
    }
  }

  @Test
  fun cancelAfterResumed() {
    runTest {
      shownScope.transition(Destroyed, Resumed)

      val async = shownScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled.shouldBeFalse()

      shownScope.transition(Resumed, Created)

      async.isCancelled.shouldBeTrue()
      async.getCancellationException().message shouldContain "Hidden"
    }
  }
}
