package com.wealthfront.magellan.sample.advanced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.tide.RootScreen

class MainActivity : AppCompatActivity() {

  private val rootScreen = RootScreen()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    app(this).injector().inject(this)
    setContentScreen(rootScreen, R.id.magellan_container)
  }
}