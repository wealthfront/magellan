package com.wealthfront.magellan.sample.advanced.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.wealthfront.magellan.sample.advanced.databinding.LoadingLayoutBinding

class LoadingLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  @AttrRes defStyleAttr: Int = 0,
  @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding = LoadingLayoutBinding.inflate(LayoutInflater.from(context), this, true)

  override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
    if (childCount == 0) {
      super.addView(child, index, params)
    } else {
      binding.content.addView(child, index, params)
    }
  }

  fun showLoading() {
    binding.progressBar.visibility = View.VISIBLE
  }

  fun hideLoading() {
    binding.progressBar.visibility = View.GONE
  }
}
