package com.wealthfront.magellan.sample.advanced

import android.app.Application
import android.content.Context
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.navigation.NavigationOverride
import com.wealthfront.magellan.navigation.NavigationOverrideProvider
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

    Magellan.init(
      navigationOverrideProvider = object : NavigationOverrideProvider {
        override fun getNavigationOverrides(): List<NavigationOverride> {
          return listOf(
            NavigationOverride(
              { _, navigable ->
                navigable is SuggestExhibitJourney
              }, { navigationDelegate, _ ->
              navigationDelegate.goTo(UpdateAppStep())
            }
            )
          )
        }
      }
    )
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
