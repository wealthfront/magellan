package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
object TestToolbarHelperModule {

  @Provides
  @Singleton
  fun provideToolbarHelper(): ToolbarHelper {
    return mock(ToolbarHelper::class.java)
  }
}
