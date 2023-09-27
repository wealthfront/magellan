package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.navigation.NavigationTraverser;
import com.wealthfront.magellan.sample.migration.api.DogApi;
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
final class TestAppModule {

  @Provides
  @Singleton
  Expedition provideExpedition() {
    return new Expedition();
  }

  @Provides
  @Singleton
  NavigationTraverser provideNavigationTraverser(Expedition root) {
    return new NavigationTraverser(root);
  }

  @Provides
  @Singleton
  DogApi provideDogApi() {
    return Mockito.mock(DogApi.class);
  }

  @Provides
  @Singleton
  ToolbarHelper provideToolbarHelper() {
    return Mockito.mock(ToolbarHelper.class);
  }
}
