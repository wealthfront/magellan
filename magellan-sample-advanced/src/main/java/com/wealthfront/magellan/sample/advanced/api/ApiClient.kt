package com.wealthfront.magellan.sample.advanced.api

import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.cerealcollection.CerealDetails
import com.wealthfront.magellan.sample.advanced.cerealcollection.CerealStatus
import com.wealthfront.magellan.sample.advanced.cerealcollection.CerealStatus.ACTIVE
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class ApiClient constructor(private val retrofit: BehaviorDelegate<ApiContract>) : ApiContract {

  val ALL_CEREALS = listOf(
    CerealDetails(
      R.string.monster_cereals_title, R.string.monster_cereals_description,
      CerealStatus.LIMITED
    ),
    CerealDetails(
      R.string.dunk_a_balls_title,
      R.string.dunk_a_balls_description,
      CerealStatus.DISCONTINUED
    ),
    CerealDetails(
      R.string.cornflakes_title,
      R.string.cornflakes_description,
      ACTIVE
    ),
    CerealDetails(
      R.string.oreo_os_title,
      R.string.oreo_os_description,
      ACTIVE
    )
  )

  companion object {

    @JvmStatic
    fun buildClient(): ApiClient {
      val retrofit = getRetrofit()
      val delegate = getMockRetrofit(retrofit).create(ApiContract::class.java)
      return ApiClient(delegate)
    }

    private fun getRetrofit(): Retrofit {
      val okHttpClient = OkHttpClient().newBuilder().build()
      return Retrofit.Builder()
        .baseUrl("http://example.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(okHttpClient)
        .build()
    }

    private fun getMockRetrofit(retrofit: Retrofit): MockRetrofit {
      val networkBehavior = NetworkBehavior.create()
      networkBehavior.setDelay(2500, TimeUnit.MILLISECONDS)
      networkBehavior.setVariancePercent(20)
      networkBehavior.setErrorPercent(0)
      networkBehavior.setFailurePercent(0)
      return MockRetrofit.Builder(retrofit)
        .networkBehavior(networkBehavior)
        .build()
    }
  }

  override fun getCollection(): Observable<List<CerealDetails>> {
    return retrofit.returningResponse(ALL_CEREALS).getCollection()
  }
}
