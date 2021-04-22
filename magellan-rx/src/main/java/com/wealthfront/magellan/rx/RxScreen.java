package com.wealthfront.magellan.rx;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import org.jetbrains.annotations.NotNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class RxScreen<V extends ViewGroup & ScreenView> extends Screen<V> {

  private CompositeSubscription subscriptions = new CompositeSubscription();

  @Override
  protected final void onShow(@NotNull Context context) {
    subscriptions = new CompositeSubscription();
    onSubscribe(context);
  }

  protected void onSubscribe(Context context) {}

  protected final void autoUnsubscribe(Subscription subscription) {
    subscriptions.add(subscription);
  }

  protected void onUnsubscribe(Context context) {}

  @Override
  protected final void onHide(@NotNull Context context) {
    onUnsubscribe(context);
    subscriptions.unsubscribe();
  }

}
