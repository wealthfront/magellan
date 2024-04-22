package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
object TestToolbarHelperModule {

  @Provides
  @Singleton
  fun provideToolbarHelper(): ToolbarHelper {
    return mockk<ToolbarHelper>(relaxed = true)
  }
}
