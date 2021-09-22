package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.tide.DogBreedsStep;
import com.wealthfront.magellan.sample.advanced.tide.DogDetailsScreen;
import com.wealthfront.magellan.sample.advanced.tide.HelpScreen;

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
