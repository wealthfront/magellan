package com.wealthfront.magellan.sample.advanced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.navigation.CurrentNavigableProvider
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var expedition: Expedition
  @Inject lateinit var currentNavigableProvider: CurrentNavigableProvider

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app(this).injector().inject(this)
    setContentScreen(expedition)
    expedition.setCurrentNavigableProvider(currentNavigableProvider)
  }

  override fun onBackPressed() {
    if (!expedition.backPressed()) {
      super.onBackPressed()
    }
  }
}
