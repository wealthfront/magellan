package com.wealthfront.magellan.sample

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

  fun inject(mainActivity: MainActivity)

  fun inject(expedition: Expedition)

  fun inject(detailStep: DetailStep)

  fun inject(firstJourney: FirstJourney)

  fun inject(secondJourney: SecondJourney)
}
