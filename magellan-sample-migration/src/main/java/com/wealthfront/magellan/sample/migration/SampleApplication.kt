package com.wealthfront.magellan.sample.migration

import android.app.Application
import android.content.Context

class SampleApplication : Application(), AppComponentContainer {

  private lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder()
      .appModule(AppModule)
      .dogApiModule(DogApiModule)
      .toolbarHelperModule(ToolbarHelperModule)
      .build()
  }

  override fun injector(): AppComponent {
    return appComponent
  }

  companion object {
    @JvmStatic
    fun app(context: Context): AppComponentContainer {
      return context.applicationContext as AppComponentContainer
    }
  }
}
