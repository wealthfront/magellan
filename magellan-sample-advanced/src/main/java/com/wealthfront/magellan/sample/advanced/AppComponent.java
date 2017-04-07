package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.tide.TideDetailsScreen;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity activity);

  void inject(TideDetailsScreen screen);

}
