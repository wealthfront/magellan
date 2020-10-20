package com.wealthfront.magellan.rx

import android.content.Context
import com.wealthfront.magellan.lifecycle.LifecycleAware
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

public class RxUnsubscriber @Inject constructor() : LifecycleAware {

  private var subscriptions: CompositeSubscription? = null

  public fun autoUnsubscribe(subscription: Subscription) {
    if (subscriptions == null) {
      subscriptions = CompositeSubscription()
    }
    subscriptions!!.add(subscription)
  }

  public fun autoUnsubscribe(vararg subscription: Subscription) {
    subscription.forEach { autoUnsubscribe(it) }
  }

  override fun hide(context: Context) {
    subscriptions?.unsubscribe()
    subscriptions = null
  }
}
