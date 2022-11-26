package com.ryanmoelter.magellanx.core.coroutines

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

public class CreatedLifecycleScope : LifecycleAware, CoroutineScope {

  private var job = SupervisorJob().apply { cancel(CancellationException("Not created yet")) }
    set(value) {
      field = value
      coroutineContext = value + Dispatchers.Main
    }

  override var coroutineContext: CoroutineContext = job + Dispatchers.Main
    private set

  override fun create() {
    job = SupervisorJob()
  }

  override fun destroy() {
    job.cancel(CancellationException("Destroyed"))
  }
}
