package com.wealthfront.magellan.rx2

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RxDisposer @Inject constructor() : LifecycleAware {

  private var disposables: CompositeDisposable? = null

  fun autoDispose(disposable: Disposable) {
    if (disposables == null) {
      disposables = CompositeDisposable()
    }
    disposables!!.add(disposable)
  }

  fun autoDispose(vararg disposable: Disposable) {
    disposable.forEach { autoDispose(it) }
  }

  override fun hide(context: Context) {
    disposables?.dispose()
    disposables = null
  }
}
