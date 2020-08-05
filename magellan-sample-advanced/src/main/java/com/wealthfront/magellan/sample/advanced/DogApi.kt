package com.wealthfront.magellan.sample.advanced

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

interface DogApi {

  @GET("https://dog.ceo/api/breed/{id}/images/random")
  fun getRandomImageForBreed(@Path("id") breed: String): Single<DogImage>
}

data class DogImage(
  @JsonProperty("message")
  val message: String,

  @JsonProperty("status")
  val status: String
)
