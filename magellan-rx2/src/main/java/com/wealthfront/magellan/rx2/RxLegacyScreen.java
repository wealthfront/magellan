package com.wealthfront.magellan.rx2;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.LegacyScreen;
import com.wealthfront.magellan.ScreenView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @deprecated use {@link RxScreen} instead.
 */
public abstract class RxLegacyScreen<V extends ViewGroup & ScreenView> extends LegacyScreen<V> {

  private CompositeDisposable disposables = new CompositeDisposable();

  @Override
  protected final void onShow(Context context) {
    disposables = new CompositeDisposable();
    onSubscribe(context);
  }

  protected void onSubscribe(Context context) {}

  protected final void autoDispose(Disposable disposable) {
    disposables.add(disposable);
  }

  protected void onDispose(Context context) {}

  @Override
  protected final void onHide(Context context) {
    onDispose(context);
    disposables.dispose();
  }

}