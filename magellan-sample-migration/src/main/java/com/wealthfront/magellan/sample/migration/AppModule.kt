package com.wealthfront.magellan.sample.migration

import com.wealthfront.magellan.navigation.NavigationTraverser
import com.wealthfront.magellan.sample.migration.api.RequestCountingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

private const val DOG_BASE_URL = "https://dog.ceo/api/"

@Module
object AppModule {

  @Provides
  @Singleton
  fun provideExpedition(): Expedition {
    return Expedition()
  }

  @Provides
  @Singleton
  fun provideNavigationTraverser(root: Expedition): NavigationTraverser {
    return NavigationTraverser(root)
  }

  @Provides
  @Singleton
  fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(DOG_BASE_URL)
      .addConverterFactory(JacksonConverterFactory.create())
      .client(httpClient)
      .build()
  }

  @Provides
  @Singleton
  fun provideHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .addInterceptor(RequestCountingInterceptor())
      .build()
  }
}
