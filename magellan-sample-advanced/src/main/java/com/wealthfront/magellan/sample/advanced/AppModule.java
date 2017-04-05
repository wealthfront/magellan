package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.wealthfront.magellan.Navigator.withRoot;

@Module
public class AppModule {

    @Provides
    @Singleton
    static Navigator provideNavigator() {
        return Navigator.withRoot(new HomeScreen()).build();
    }

}
