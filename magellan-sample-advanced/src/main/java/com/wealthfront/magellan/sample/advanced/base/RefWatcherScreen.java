package com.wealthfront.magellan.sample.advanced.base;

import android.content.Context;
import android.view.ViewGroup;
import com.wealthfront.magellan.ScreenView;
import com.wealthfront.magellan.rx.RxScreen;
import com.wealthfront.magellan.sample.advanced.BuildConfig;
import com.wealthfront.magellan.sample.advanced.SampleApplication;

public abstract class RefWatcherScreen<V extends ViewGroup & ScreenView> extends RxScreen<V> {

  public final void onHide(Context context) {
    super.onHide(context);
    if (BuildConfig.DEBUG) {
      SampleApplication.app(context).refWatcher().watch(this);
    }
  }

}
