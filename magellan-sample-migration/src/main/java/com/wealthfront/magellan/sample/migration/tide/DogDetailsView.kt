package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.VisibleForTesting
import com.bumptech.glide.Glide
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.sample.migration.databinding.DogDetailsBinding

@OpenForMocking
class DogDetailsView(context: Context) : BaseScreenView<DogDetailsScreen>(context) {

  @VisibleForTesting
  var glideBuilder = Glide.with(this)

  @VisibleForTesting
  val viewBinding = DogDetailsBinding.inflate(LayoutInflater.from(context), this, true)

  fun setDogPic(dogUrl: String) {
    glideBuilder
      .load(dogUrl)
      .into(viewBinding.dogImage)
  }
}
