package com.wealthfront.magellan.sample.migration.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.wealthfront.magellan.sample.migration.R
import com.wealthfront.magellan.sample.migration.databinding.ToolbarBinding

class ToolbarView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  val binding = ToolbarBinding.inflate(LayoutInflater.from(context), this, true)

  fun setTitle(title: CharSequence) {
    binding.title.text = title
  }

  fun setMenuColor(color: Int) {
    binding.menuBar.setBackgroundColor(context.resources.getColor(color))
  }

  fun setMenuIcon(icon: Int, onClickListener: () -> Unit) {
    binding.menuIcon.visibility = VISIBLE
    binding.menuIcon.setImageResource(icon)
    binding.menuIcon.setOnClickListener { onClickListener.invoke() }
  }

  fun reset() {
    binding.menuBar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
    binding.title.text = ""
    binding.menuIcon.visibility = GONE
  }
}
