package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.bumptech.glide.Glide
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.sample.migration.R

class DogDetailsView(context: Context) : BaseScreenView<DogDetailsScreen>(context) {

  @BindView(R.id.dogImage) lateinit var dogImage: ImageView

  init {
    inflate(R.layout.dog_details)
    bind(this)
    dogImage.setOnLongClickListener { image ->
      screen.goToHelpScreen(image)
      return@setOnLongClickListener true
    }
  }

  fun setDogPic(dogUrl: String) {
    Glide.with(this)
      .load(dogUrl)
      .into(dogImage)
  }
}
