package com.wealthfront.magellan.sample.migration.tide

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import com.bumptech.glide.Glide
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.sample.migration.databinding.HelpBinding

@SuppressLint("ViewConstructor")
@OpenForMocking
class HelpView(context: Context, private val screen: HelpScreen) : FrameLayout(context) {

  @VisibleForTesting
  val binding = HelpBinding.inflate(LayoutInflater.from(context), this, true)
  @VisibleForTesting
  var glideBuilder = Glide.with(this)

  init {
    binding.dialog.setOnClickListener {
      screen.showHelpDialog()
    }
  }

  fun setDogPic(dogUrl: String) {
    glideBuilder
      .load(dogUrl)
      .into(binding.dogImage)
  }
}
