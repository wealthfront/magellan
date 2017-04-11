package com.wealthfront.magellan.rx2;

import android.content.Context;
import android.view.ViewGroup;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.google.common.truth.Truth.assertThat;

public class RxScreenTest {

  RxScreen rxScreen = new DummyRxScreen();

  @Test
  public void autoDispose() {
    Disposable disposable = Observable.just("a").subscribe();
    rxScreen.autoDispose(disposable);
    rxScreen.onHide(null);
    assertThat(disposable.isDisposed()).isTrue();
  }

  public class DummyRxScreen extends RxScreen {

    @Override
    protected ViewGroup createView(Context context) {
      return null;
    }

  }

}