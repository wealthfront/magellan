package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import butterknife.ButterKnife.bind
import butterknife.OnClick
import com.wealthfront.magellan.BaseScreenView
import com.wealthfront.magellan.sample.advanced.R

class HelpView(context: Context) : BaseScreenView<HelpScreen>(context) {

  init {
    inflate(R.layout.help)
    bind(this)
  }

  @OnClick(R.id.dialog)
  fun showHelpDialog() {
    screen.showHelpDialog()
  }
}
