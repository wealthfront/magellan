package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.api.DogApi
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
object TestDogApiModule {

  @Provides
  @Singleton
  fun provideDogApi(): DogApi {
    return mock(DogApi::class.java)
  }
}
