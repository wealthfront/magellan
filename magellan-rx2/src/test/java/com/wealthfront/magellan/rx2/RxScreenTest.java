package com.wealthfront.magellan.rx2;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.navigation.NavigableCompat;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import androidx.annotation.NonNull;
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