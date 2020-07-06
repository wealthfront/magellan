package com.wealthfront.magellan.sample.advanced

import android.app.Application
import android.content.Context

class SampleApplication : Application() {

  private lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
  }

  fun injector(): AppComponent {
    return appComponent
  }

  companion object {
    @JvmStatic
    fun app(context: Context): SampleApplication {
      return context.applicationContext as SampleApplication
    }
  }
}