package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.tide.DogListStepFactory
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, TestDogApiModule::class, TestToolbarHelperModule::class])
@Singleton
interface TestAppComponent : AppComponent {

  val toolbarHelper: ToolbarHelper
  val api: DogApi
  val dogListStepFactory: DogListStepFactory
}
