package com.wealthfront.magellan

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
public class SyncLifecycleScope : LifecycleAware, CoroutineScope {
  private val testCoroutineDispatcher = TestCoroutineDispatcher()
  private var testCoroutineScope: TestCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

  override var coroutineContext: CoroutineContext = testCoroutineScope.coroutineContext

  override fun show(context: Context) {
    testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)
  }

  override fun hide(context: Context) {
    testCoroutineScope.cancel(CancellationException("Hidden"))
  }
}