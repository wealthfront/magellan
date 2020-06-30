package com.wealthfront.magellan.rx;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.LegacyScreen;
import com.wealthfront.magellan.ScreenView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @deprecated use {@link RxScreen} instead.
 */
public abstract class RxLegacyScreen<V extends ViewGroup & ScreenView> extends LegacyScreen<V> {

  private CompositeSubscription subscriptions = new CompositeSubscription();

  @Override
  protected final void onShow(Context context) {
    subscriptions = new CompositeSubscription();
    onSubscribe(context);
  }

  protected void onSubscribe(Context context) {}

  protected final void autoUnsubscribe(Subscription subscription) {
    subscriptions.add(subscription);
  }

  protected void onUnsubscribe(Context context) {}

  @Override
  protected final void onHide(Context context) {
    onUnsubscribe(context);
    subscriptions.unsubscribe();
  }

}
