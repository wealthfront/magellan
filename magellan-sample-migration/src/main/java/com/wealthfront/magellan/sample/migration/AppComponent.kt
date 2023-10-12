package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreen
import com.wealthfront.magellan.sample.migration.tide.DogListStep
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DogApiModule::class, ToolbarHelperModule::class])
@Singleton
interface AppComponent {

  fun inject(activity: MainActivity)
  fun inject(step: DogListStep)
  fun inject(screen: DogDetailsScreen)
  fun inject(expedition: Expedition)
}
