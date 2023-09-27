package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.sample.migration.tide.DogBreedsStepTest;
import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreenTest;
import com.wealthfront.magellan.sample.migration.tide.HelpScreenTest;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = TestAppModule.class)
@Singleton
public interface TestAppComponent extends AppComponent {
  void inject(HelpScreenTest test);

  void inject(DogBreedsStepTest test);

  void inject(DogDetailsScreenTest test);
}
