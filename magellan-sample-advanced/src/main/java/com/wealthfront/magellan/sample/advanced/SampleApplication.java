package com.wealthfront.magellan.sample.advanced;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import dagger.Component;

public class SampleApplication extends Application {

  public static SampleApplication app(Context context) {
    return (SampleApplication) context.getApplicationContext();
  }

  public AppComponent injector() {
    return DaggerAppComponent.create();
  }

}
