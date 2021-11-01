package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife.bind
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.wealthfront.magellan.sample.migration.R

class HelpView(context: Context, private val screen: HelpScreen) : FrameLayout(context) {

  @BindView(R.id.dogImage) lateinit var dogImage: ImageView

  init {
    inflate(getContext(), R.layout.help, this)
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
