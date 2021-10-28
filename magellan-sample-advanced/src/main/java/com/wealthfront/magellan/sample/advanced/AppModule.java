package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.api.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

  @Provides
  @Singleton
  RootJourney provideRootJourney() {
    return new RootJourney();
  }

  @Provides
  @Singleton
  ApiClient provideApiClient() {
    return ApiClient.buildClient();
  }

  @Provides
  @Singleton
  ToolbarHelper provideToolbarHelper() {
    return new ToolbarHelper();
  }
}
