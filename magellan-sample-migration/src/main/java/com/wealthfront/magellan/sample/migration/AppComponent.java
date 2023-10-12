package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreen;
import com.wealthfront.magellan.sample.migration.tide.DogListStep;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity activity);

  void inject(DogListStep step);

  void inject(DogDetailsScreen screen);

  void inject(Expedition expedition);
}
