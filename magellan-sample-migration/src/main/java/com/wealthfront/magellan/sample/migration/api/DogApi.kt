package com.wealthfront.magellan.sample.migration.api

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {

  @GET("breed/retriever/list")
  suspend fun getListOfAllBreedsOfRetriever(): DogBreeds

  @GET("breed/{id}/images/random")
  suspend fun getRandomImageForBreed(@Path("id") breed: String): DogMessage
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
