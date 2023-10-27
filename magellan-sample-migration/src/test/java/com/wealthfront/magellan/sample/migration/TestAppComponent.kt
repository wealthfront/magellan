package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreenTest
import com.wealthfront.magellan.sample.migration.tide.DogListStepTest
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, TestDogApiModule::class, TestToolbarHelperModule::class])
@Singleton
interface TestAppComponent : AppComponent {

  val toolbarHelper: ToolbarHelper
  val api: DogApi

  fun inject(test: DogDetailsScreenTest)
  fun inject(test: DogListStepTest)
}
