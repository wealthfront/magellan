package com.wealthfront.magellan.rx;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.ViewGroup;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class RxScreen<V extends ViewGroup & ScreenView> extends Screen<V> {

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

  @CallSuper
  public void onHide(Context context) {
    onUnsubscribe(context);
    subscriptions.unsubscribe();
  }

}
