package com.wealthfront.magellan.sample.migration

import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DogApiModule::class, ToolbarHelperModule::class])
@Singleton
interface AppComponent {

  fun inject(activity: MainActivity)
  fun inject(expedition: Expedition)
}
