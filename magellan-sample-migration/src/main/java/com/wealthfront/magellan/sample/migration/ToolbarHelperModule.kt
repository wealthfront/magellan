package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ToolbarHelperModule {

  @Provides
  @Singleton
  fun provideToolbarHelper(): ToolbarHelper {
    return ToolbarHelper()
  }
}
