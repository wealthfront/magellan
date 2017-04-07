package com.wealthfront.magellan.sample.advanced;

import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {

  public static SampleApplication app(Context context) {
    return (SampleApplication) context.getApplicationContext();
  }

  public AppComponent injector() {
    return DaggerAppComponent.create();
  }

}
