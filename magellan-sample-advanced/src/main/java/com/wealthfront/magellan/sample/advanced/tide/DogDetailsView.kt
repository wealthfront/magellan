package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.bumptech.glide.Glide
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.sample.advanced.R

class DogDetailsView(context: Context) : BaseScreenView<DogDetailsScreen>(context) {

  @BindView(R.id.dogImage) lateinit var dogImage: ImageView

  init {
    inflate(R.layout.dog_details)
    bind(this)
  }

  fun setDogPic(dogUrl: String) {
    Glide.with(this)
      .load(dogUrl)
      .into(dogImage)
  }
}
