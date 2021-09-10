package com.wealthfront.magellan.coroutines

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

public class CreatedLifecycleScope @Inject constructor() : LifecycleAware, CoroutineScope {

  private var job = SupervisorJob().apply { cancel(CancellationException("Not created yet")) }
    set(value) {
      field = value
      coroutineContext = value + Dispatchers.Main
    }

  override var coroutineContext: CoroutineContext = job + Dispatchers.Main
    private set

  override fun create(context: Context) {
    job = SupervisorJob()
  }

  override fun destroy(context: Context) {
    job.cancel(CancellationException("Destroyed"))
  }
}
