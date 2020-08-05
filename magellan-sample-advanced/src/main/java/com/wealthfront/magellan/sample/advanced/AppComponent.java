package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.tide.DogDetailsScreen;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity activity);

  void inject(DogDetailsScreen screen);

  void inject(Expedition expedition);
}
