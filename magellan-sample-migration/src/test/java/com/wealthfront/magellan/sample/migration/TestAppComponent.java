package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.sample.migration.tide.HelpScreenTest;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = TestAppModule.class)
@Singleton
public interface TestAppComponent extends AppComponent {
  void inject(HelpScreenTest test);
}
