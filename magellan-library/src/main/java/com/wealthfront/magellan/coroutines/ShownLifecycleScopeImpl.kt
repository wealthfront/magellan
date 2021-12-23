package com.wealthfront.magellan.coroutines

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

public interface ShownLifecycleScope: LifecycleAware, CoroutineScope

public class ShownLifecycleScopeImpl @Inject constructor() : ShownLifecycleScope {

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
