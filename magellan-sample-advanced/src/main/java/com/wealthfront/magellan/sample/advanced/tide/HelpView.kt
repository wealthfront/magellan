package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife.bind
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.sample.advanced.R

class HelpView(context: Context) : BaseScreenView<HelpScreen>(context) {

  @BindView(R.id.dogImage) lateinit var dogImage: ImageView

  init {
    inflate(R.layout.help)
    bind(this)
  }

  fun setDogPic(dogUrl: String) {
    Glide.with(this)
      .load(dogUrl)
      .into(dogImage)
  }

  @OnClick(R.id.dialog)
  fun showHelpDialog() {
    screen.showHelpDialog()
  }
}
