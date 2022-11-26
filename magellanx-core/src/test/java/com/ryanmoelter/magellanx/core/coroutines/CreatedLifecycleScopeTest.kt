package com.ryanmoelter.magellanx.core.coroutines

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.transition
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
internal class CreatedLifecycleScopeTest {

  private lateinit var createdScope: CreatedLifecycleScope

  @Before
  fun setUp() {
    createdScope = CreatedLifecycleScope()
    Dispatchers.setMain(Dispatchers.Default.limitedParallelism(1))
  }

  @Test
  fun cancelBeforeCreated() {
    runTest {
      val async = createdScope.async { delay(5000) }
      async.isCancelled shouldBe true
      async.getCancellationException().message shouldContain "Not created yet"

      createdScope.transition(Destroyed, Resumed)

      async.isCancelled shouldBe true

      createdScope.transition(Resumed, Created)

      async.isCancelled shouldBe true
      async.getCancellationException().message shouldContain "Not created yet"
    }
  }

  @Test
  fun cancelAfterCreated() {
    runTest {
      createdScope.transition(Destroyed, Created)

      val async = createdScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled shouldBe false

      createdScope.transition(Created, Resumed)

      async.isCancelled shouldBe false

      createdScope.transition(Resumed, Created)

      async.isCancelled shouldBe false

      createdScope.transition(Created, Destroyed)

      async.isCancelled shouldBe true
      async.getCancellationException().message shouldContain "Destroyed"
    }
  }

  @Test
  fun cancelAfterShown() {
    runTest {
      createdScope.transition(Destroyed, Shown)

      val async = createdScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled shouldBe false

      createdScope.transition(Shown, Resumed)

      async.isCancelled shouldBe false

      createdScope.transition(Resumed, Created)

      async.isCancelled shouldBe false

      createdScope.transition(Created, Destroyed)

      async.isCancelled shouldBe true
      async.getCancellationException().message shouldContain "Destroyed"
    }
  }

  @Test
  fun cancelAfterResumed() {
    runTest {
      createdScope.transition(Destroyed, Resumed)

      val async = createdScope.async(Dispatchers.Default) { delay(5000) }
      async.isCancelled shouldBe false

      createdScope.transition(Resumed, Created)

      async.isCancelled shouldBe false

      createdScope.transition(Created, Destroyed)
      async.isCancelled shouldBe true
      async.getCancellationException().message shouldContain "Destroyed"
    }
  }
}
