package com.wealthfront.magellan.sample

import android.app.Application
import com.wealthfront.magellan.sample.App.Provider.appComponent

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder()
      .appModule(AppModule(applicationContext))
      .build()
  }

  object Provider {
    lateinit var appComponent: AppComponent
  }
}
