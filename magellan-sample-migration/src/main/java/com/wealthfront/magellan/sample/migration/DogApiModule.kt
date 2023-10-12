package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.sample.migration.api.DogApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object DogApiModule {

  @Provides
  @Singleton
  fun provideDogApi(retrofit: Retrofit): DogApi {
    return retrofit.create(DogApi::class.java)
  }
}
