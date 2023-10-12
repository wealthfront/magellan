package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.uitest.NavigationTest
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ToolbarHelperModule::class, TestDogApiModule::class])
@Singleton
interface TestAppComponent : AppComponent {

  fun inject(test: NavigationTest)
}
