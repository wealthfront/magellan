package com.wealthfront.magellan.rx;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.navigation.NavigableCompat;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import androidx.annotation.NonNull;
import rx.Observable;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

public class RxScreenTest {

  RxScreen rxScreen = new DummyRxScreen();

  @Test
  public void autoUnsubscribe() {
    Subscription subscription = Observable.just("a").subscribe();
    rxScreen.autoUnsubscribe(subscription);
    rxScreen.onHide(null);
    assertThat(subscription.isUnsubscribed()).isTrue();
  }

  public class DummyRxScreen extends RxScreen {

    @Override
    protected ViewGroup createView(@NotNull Context context) {
      return null;
    }

    @NonNull
    @Override
    public NavigableCompat getCurrentNavigable() {
      return this;
    }
  }

}