package com.wealthfront.magellan.sample.advanced

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var rootJourney: RootJourney
  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app(this).injector().inject(this)
    setContentScreen(rootJourney, R.layout.activity_main)
    lifecycle.addObserver(toolbarHelper)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(toolbarHelper)
  }

  override fun onBackPressed() {
    if (!rootJourney.backPressed()) {
      super.onBackPressed()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when {
      (item.itemId == android.R.id.home) -> {
        onBackPressed()
        true
      }
      else -> false
    }
  }
}
