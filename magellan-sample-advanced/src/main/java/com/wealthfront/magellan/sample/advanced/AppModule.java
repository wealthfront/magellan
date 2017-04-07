package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class AppModule {

  private static final String NOAA_API_BASE_URL = "https://tidesandcurrents.noaa.gov/";

  @Provides
  @Singleton
  static Navigator provideNavigator() {
    return Navigator.withRoot(new TideLocationsScreen()).build();
  }

  @Provides
  @Singleton
  static NoaaApi provideNoaaApi(Retrofit retrofit) {
    return retrofit.create(NoaaApi.class);
  }

  @Provides
  @Singleton
  static Retrofit provideRetrofit(OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .baseUrl(NOAA_API_BASE_URL)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .client(httpClient)
        .build();
  }

  @Provides
  @Singleton
  static OkHttpClient provideHttpClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return new OkHttpClient.Builder().addInterceptor(interceptor).build();
  }

}
