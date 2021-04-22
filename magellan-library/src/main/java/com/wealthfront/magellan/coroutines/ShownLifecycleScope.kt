package com.wealthfront.magellan.coroutines

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

public class ShownLifecycleScope @Inject constructor() : LifecycleAware, CoroutineScope {

  private var job = SupervisorJob().apply { cancel(CancellationException("Not created yet")) }

  override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

  override fun create(context: Context) {
    job = SupervisorJob()
  }

  override fun show(context: Context) {
    if (job.isCancelled) {
      job = SupervisorJob()
    }
  }

  override fun hide(context: Context) {
    job.cancel(CancellationException("Hidden"))
  }
}
