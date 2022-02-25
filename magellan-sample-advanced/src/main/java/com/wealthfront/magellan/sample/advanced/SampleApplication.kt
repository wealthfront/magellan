package com.wealthfront.magellan.sample.advanced

import android.app.Application
import android.content.Context
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationDelegate
import com.wealthfront.magellan.navigation.NavigationRequestHandler
import com.wealthfront.magellan.navigation.goTo
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitJourney
import com.wealthfront.magellan.sample.advanced.update.UpdateAppStep

class SampleApplication : Application() {

  private lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder()
      .appModule(AppModule())
      .build()

    Magellan.navigationRequestHandler = object : NavigationRequestHandler {
      override fun overrideNavigationRequest(
        navigationDelegate: NavigationDelegate,
        navigable: NavigableCompat
      ) {
        navigationDelegate.goTo(UpdateAppStep())
      }

      override fun shouldOverrideNavigation(
        navigationDelegate: NavigationDelegate,
        navigable: NavigableCompat
      ): Boolean {
        return navigable is SuggestExhibitJourney
      }
    }
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
