package com.wealthfront.magellan.sample.advanced.api

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface DogApi {

  @GET("breed/retriever/list")
  suspend fun getListOfAllBreedsOfRetriever(): DogBreeds

  @GET("breed/{id}/images/random")
  fun getRandomImageForBreed(@Path("id") breed: String): Observable<DogMessage>
}

data class DogMessage(
  @JsonProperty("message")
  val message: String,

  @JsonProperty("status")
  val status: String
)

data class DogBreeds(
  @JsonProperty("message")
  val message: List<String>,

  @JsonProperty("status")
  val status: String
)
