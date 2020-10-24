package com.wealthfront.magellan.rx2

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

public class RxDisposer @Inject constructor() : LifecycleAware {

  private var disposables: CompositeDisposable? = null

  public fun autoDispose(disposable: Disposable) {
    if (disposables == null) {
      disposables = CompositeDisposable()
    }
    disposables!!.add(disposable)
  }

  public fun autoDispose(vararg disposable: Disposable) {
    disposable.forEach { autoDispose(it) }
  }

  override fun hide(context: Context) {
    disposables?.dispose()
    disposables = null
  }
}
