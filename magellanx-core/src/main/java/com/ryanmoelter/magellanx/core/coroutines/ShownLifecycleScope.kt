package com.ryanmoelter.magellanx.core.coroutines

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

public class ShownLifecycleScope : LifecycleAware, CoroutineScope {

  private var job = SupervisorJob().apply { cancel(CancellationException("Not shown yet")) }
    set(value) {
      field = value
      coroutineContext = value + Dispatchers.Main
    }

  override var coroutineContext: CoroutineContext = job + Dispatchers.Main
    private set

  override fun show() {
    job = SupervisorJob()
  }

  override fun hide() {
    job.cancel(CancellationException("Hidden"))
  }
}
