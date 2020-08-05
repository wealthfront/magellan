package com.wealthfront.magellan.sample.advanced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var expedition: Expedition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app(this).injector().inject(this)
    setContentScreen(expedition)
  }

  override fun onBackPressed() {
    if (!expedition.backPressed()) {
      super.onBackPressed()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    expedition.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }
}
