package com.wealthfront.magellan.sample.migration.api

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {

  @GET("breed/{id}/list")
  suspend fun getAllSubBreeds(@Path("id") breed: String): DogSubBreedsResponse

  @GET("breeds/list/all")
  suspend fun getAllBreeds(): DogBreedsResponse

  @GET("breed/{id}}/images")
  suspend fun getAllImagesForBreed(@Path("id") breed: String): DogImagesResposne

  @GET("breed/{id}/images/random")
  suspend fun getRandomImageForBreed(@Path("id") breed: String): DogImageResponse
}

data class DogImageResponse(
  @JsonProperty("message")
  val message: String,

  @JsonProperty("status")
  val status: String
)

data class DogSubBreedsResponse(
  @JsonProperty("message")
  val message: List<String>,

  @JsonProperty("status")
  val status: String
)

data class DogImagesResposne(
  @JsonProperty("message")
  val message: List<String>,

  @JsonProperty("status")
  val status: String
)

data class DogBreedsResponse(
  @JsonProperty("message")
  val message: Map<String, List<String>>,

  @JsonProperty("status")
  val status: String
)
