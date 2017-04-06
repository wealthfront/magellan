package com.wealthfront.magellan.sample.advanced;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
interface AppComponent {

    void inject(MainActivity activity);

    void inject(TideDetailsScreen screen);

}
