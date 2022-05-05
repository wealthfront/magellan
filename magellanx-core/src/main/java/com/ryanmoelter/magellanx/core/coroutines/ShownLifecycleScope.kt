package com.ryanmoelter.magellanx.core.coroutines

import android.content.Context
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

  override fun show(context: Context) {
    job = SupervisorJob()
  }

  override fun hide(context: Context) {
    job.cancel(CancellationException("Hidden"))
  }
}
