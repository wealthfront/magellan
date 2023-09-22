package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.sample.migration.databinding.DogDetailsBinding

class DogDetailsView(context: Context) : BaseScreenView<DogDetailsScreen>(context) {

  private val viewBinding = DogDetailsBinding.inflate(LayoutInflater.from(context), this, true)

  init {
    viewBinding.dogImage.setOnLongClickListener { image ->
      screen.goToHelpScreen(image)
      return@setOnLongClickListener true
    }
  }

  fun setDogPic(dogUrl: String) {
    Glide.with(this)
      .load(dogUrl)
      .into(viewBinding.dogImage)
  }
}
