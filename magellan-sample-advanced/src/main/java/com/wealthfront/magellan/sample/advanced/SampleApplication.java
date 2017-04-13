package com.wealthfront.magellan.sample.advanced;

import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {

  private AppComponent appComponent;

  public static SampleApplication app(Context context) {
    return (SampleApplication) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.create();
  }

  public AppComponent injector() {
    return appComponent;
  }

}
