package com.wealthfront.magellan.sample.advanced;

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class SampleApplication extends Application {

  private AppComponent appComponent;
  private RefWatcher refWatcher;

  public static SampleApplication app(Context context) {
    return (SampleApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    refWatcher = LeakCanary.install(this);
    appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
  }

  public AppComponent injector() {
    return appComponent;
  }

  public RefWatcher refWatcher() {
    return refWatcher;
  }

}
