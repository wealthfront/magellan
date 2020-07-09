package com.wealthfront.magellan.sample.advanced

import android.content.Context
import com.wealthfront.magellan.compose.core.Screen
import com.wealthfront.magellan.sample.advanced.tide.RootScreen
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module
internal class AppModule(private val context: Context) {

  @Provides
  fun provideContext(): Context {
    return context
  }

  @Provides
  @Singleton
  fun provideRootScreen(): Screen<*> {
    return RootScreen()
  }

  @Provides
  @Singleton
  fun provideNoaaApi(retrofit: Retrofit): NoaaApi {
    return retrofit.create(NoaaApi::class.java)
  }

  @Provides
  @Singleton
  fun provideRetrofit(httpClient: OkHttpClient?): Retrofit {
    return Retrofit.Builder()
      .baseUrl(NOAA_API_BASE_URL)
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(JacksonConverterFactory.create())
      .client(httpClient)
      .build()
  }

  @Provides
  @Singleton
  fun provideHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
  }

  companion object {
    private const val NOAA_API_BASE_URL = "https://tidesandcurrents.noaa.gov/"
  }
}