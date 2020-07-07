package com.wealthfront.magellan.sample.advanced;

import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {

  private static AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
  }

  public static AppComponent injector() {
    return appComponent;
  }

}
