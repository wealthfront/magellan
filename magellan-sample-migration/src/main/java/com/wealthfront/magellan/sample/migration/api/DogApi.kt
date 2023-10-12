package com.wealthfront.magellan.sample.migration.api

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {

  @GET("breed/{id}/list")
  suspend fun getAllSubBreeds(@Path("id") breed: String): DogSubBreeds

  @GET("/breeds/list/all")
  suspend fun getAllBreeds(): DogBreeds

  @GET("/breed/{id}}/images")
  suspend fun getAllBreeds(@Path("id") breed: String): DogImages

  @GET("breed/{id}/images/random")
  suspend fun getRandomImageForBreed(@Path("id") breed: String): DogMessage
}

data class DogMessage(
  @JsonProperty("message")
  val message: String,

  @JsonProperty("status")
  val status: String
)

data class DogSubBreeds(
  @JsonProperty("message")
  val message: List<String>,

  @JsonProperty("status")
  val status: String
)

data class DogImages(
  @JsonProperty("message")
  val message: List<String>,

  @JsonProperty("status")
  val status: String
)

data class DogBreeds(
  @JsonProperty("message")
  val message: Map<String, List<String>>,

  @JsonProperty("status")
  val status: String
)
