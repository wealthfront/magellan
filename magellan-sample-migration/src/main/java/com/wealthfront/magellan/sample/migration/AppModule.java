package com.wealthfront.magellan.sample.migration;

import com.wealthfront.magellan.navigation.NavigationTraverser;
import com.wealthfront.magellan.sample.migration.api.DogApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
final class AppModule {

  private static final String DOG_BASE_URL = "https://dog.ceo/api/";

  @Provides
  @Singleton
  Expedition provideExpedition() {
    return new Expedition();
  }

  @Provides
  @Singleton
  NavigationTraverser provideNavigationTraverser(Expedition root) {
    return new NavigationTraverser(root);
  }

  @Provides
  @Singleton
  DogApi provideDogApi(Retrofit retrofit) {
    return retrofit.create(DogApi.class);
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .baseUrl(DOG_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(JacksonConverterFactory.create())
        .client(httpClient)
        .build();
  }

  @Provides
  @Singleton
  OkHttpClient provideHttpClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return new OkHttpClient.Builder().addInterceptor(interceptor).build();
  }

}
