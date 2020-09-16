package com.wealthfront.magellan.rx2;

import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.LazyTabsScreenGroup;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class LazyTabsRxScreenGroup<S extends Screen, V extends ViewGroup & ScreenView> extends LazyTabsScreenGroup<S, V> {

  private CompositeDisposable compositeSubscription = new CompositeDisposable();

  public LazyTabsRxScreenGroup() {
    super();
  }

  public LazyTabsRxScreenGroup(List<S> screens) {
    super(screens);
  }

  @Override
  protected void onShow(@NotNull Context context) {
    compositeSubscription = new CompositeDisposable();
    super.onShow(context);
    onSubscribe(context);
  }

  protected void onSubscribe(@NotNull Context context) {
  }

  protected final void autoDispose(Disposable disposable) {
    compositeSubscription.add(disposable);
  }

  protected void onDispose(@NotNull Context context) {
  }

  @Override
  protected final void onHide(@NotNull Context context) {
    onDispose(context);
    compositeSubscription.dispose();
    super.onHide(context);
  }

}