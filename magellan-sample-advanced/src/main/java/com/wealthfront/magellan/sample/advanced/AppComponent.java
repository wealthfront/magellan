package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.tide.TideDetailsScreen;
import dagger.Component;
import javax.inject.Singleton;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity activity);

  void inject(TideDetailsScreen screen);
}
