package com.wealthfront.magellan.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen

class MainActivity : AppCompatActivity() {

  private val expeditionProvider = ExpeditionProvider

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentScreen(expeditionProvider.expedition)
  }

  override fun onBackPressed() {
    if (!expeditionProvider.expedition.backPressed()) {
      super.onBackPressed()
    }
  }
}
