package com.wealthfront.magellan.sample.advanced

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

interface DogApi {

  @GET("https://dog.ceo/api/breed/retriever/list")
  fun getListOfAllBreedsOfRetriever(): Single<DogBreeds>

  @GET("https://dog.ceo/api/breed/{id}/images/random")
  fun getRandomImageForBreed(@Path("id") breed: String): Single<DogMessage>
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
