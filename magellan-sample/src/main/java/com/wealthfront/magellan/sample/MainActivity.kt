package com.wealthfront.magellan.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.magellan.compose.setContentNavigable
import com.wealthfront.magellan.sample.App.Provider.appComponent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  @Inject lateinit var expedition: Expedition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appComponent.inject(this)
    setContentNavigable(expedition)
  }

  override fun onBackPressed() {
    if (!expedition.backPressed()) {
      super.onBackPressed()
    }
  }
}
