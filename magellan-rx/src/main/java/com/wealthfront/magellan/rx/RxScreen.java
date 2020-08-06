package com.wealthfront.magellan.rx;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import androidx.annotation.NonNull;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class RxScreen<V extends ViewGroup & ScreenView> extends Screen<V> {

  private CompositeSubscription subscriptions = new CompositeSubscription();

  @Override
  protected final void onShow(@NonNull Context context) {
    super.onShow(context);
    subscriptions = new CompositeSubscription();
    onSubscribe(context);
  }

  protected void onSubscribe(Context context) {}

  protected final void autoUnsubscribe(Subscription subscription) {
    subscriptions.add(subscription);
  }

  protected void onUnsubscribe(Context context) {}

  @Override
  protected final void onHide(@NonNull Context context) {
    super.onHide(context);
    onUnsubscribe(context);
    subscriptions.unsubscribe();
  }

}
