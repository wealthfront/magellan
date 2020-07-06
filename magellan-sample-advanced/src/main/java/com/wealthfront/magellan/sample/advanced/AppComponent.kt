package com.wealthfront.magellan.sample.advanced

import com.wealthfront.magellan.sample.advanced.tide.TideDetailsScreen
import com.wealthfront.magellan.sample.advanced.tide.TideLocationsScreen
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

  fun inject(activity: MainActivity)

  fun inject(screen: TideDetailsScreen)

  fun inject(screen: TideLocationsScreen)
}