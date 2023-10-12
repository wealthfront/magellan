package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreenTest
import com.wealthfront.magellan.sample.migration.tide.DogListStepTest
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, TestDogApiModule::class, TestToolbarHelperModule::class])
@Singleton
interface TestAppComponent : AppComponent {

  fun inject(test: DogDetailsScreenTest)
  fun inject(test: DogListStepTest)
}
