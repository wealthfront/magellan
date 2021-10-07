package com.wealthfront.magellan.sample.advanced.api

import com.wealthfront.magellan.sample.advanced.cerealcollection.CerealDetails
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiContract {

  @GET("v1/cereals")
  fun getCollection(): Observable<List<CerealDetails>>
}
