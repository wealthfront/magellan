package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.sample.migration.tide.DogBreedsStep;
import com.wealthfront.magellan.sample.migration.tide.DogDetailsScreen;
import com.wealthfront.magellan.sample.migration.tide.HelpScreen;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity activity);

  void inject(DogDetailsScreen screen);

  void inject(DogBreedsStep step);

  void inject(HelpScreen screen);

  void inject(Expedition expedition);
}
