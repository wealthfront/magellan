package com.wealthfront.magellan.rx2;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class RxScreen<V extends ViewGroup & ScreenView> extends Screen<V> {

  private CompositeDisposable disposables = new CompositeDisposable();

  @Override
  protected final void onShow(@NonNull Context context) {
    disposables = new CompositeDisposable();
    onSubscribe(context);
  }

  protected void onSubscribe(@NonNull Context context) {}

  protected final void autoDispose(Disposable disposable) {
    disposables.add(disposable);
  }

  protected void onDispose(@NonNull Context context) {}

  @Override
  protected final void onHide(@NonNull Context context) {
    onDispose(context);
    disposables.dispose();
  }

}
