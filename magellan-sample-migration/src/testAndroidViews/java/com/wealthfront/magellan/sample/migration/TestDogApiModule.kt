package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.api.DogApi
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
object TestDogApiModule {

  @Provides
  @Singleton
  fun provideDogApi(): DogApi {
    return mockk<DogApi>(relaxed = true)
  }
}
