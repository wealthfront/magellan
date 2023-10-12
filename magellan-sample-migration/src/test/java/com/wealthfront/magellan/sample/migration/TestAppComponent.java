package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreenTest;
import com.wealthfront.magellan.sample.migration.tide.DogListStepTest;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = TestAppModule.class)
@Singleton
public interface TestAppComponent extends AppComponent {
  void inject(DogDetailsScreenTest test);

  void inject(DogListStepTest test);
}
