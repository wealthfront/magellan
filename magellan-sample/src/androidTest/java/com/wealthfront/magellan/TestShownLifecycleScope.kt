package com.wealthfront.magellan

import android.content.Context
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
public class TestShownLifecycleScope : ShownLifecycleScope {
  private val testCoroutineDispatcher = TestCoroutineDispatcher()
  private var testCoroutineScope: TestCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

  private var job = SupervisorJob().apply { cancel(CancellationException("Not shown yet")) }
    set(value) {
      field = value
      coroutineContext = value + testCoroutineDispatcher
    }

  override var coroutineContext: CoroutineContext = job + testCoroutineScope.coroutineContext


  override fun show(context: Context) {
    job = SupervisorJob()
  }

  override fun hide(context: Context) {
    job.cancel(CancellationException("Hidden"))
  }
}