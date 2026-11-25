package com.wealthfront.magellan.sample.migration

import android.app.Application

class TestSampleApplication : Application(), AppComponentContainer {

  private lateinit var appComponent: TestAppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerTestAppComponent.create()
  }

  override fun injector(): AppComponent {
    return appComponent
  }
}
