package com.wealthfront.magellan.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.App.Provider.appComponent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  @Inject lateinit var expedition: Expedition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appComponent.inject(this)
    setContentScreen(expedition)
  }

  override fun onBackPressed() {
    if (!expedition.backPressed()) {
      super.onBackPressed()
    }
  }
}
