package com.wealthfront.magellan.sample.advanced

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.ActionBarConfig
import com.wealthfront.magellan.NavigationListener
import com.wealthfront.magellan.compose.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.tide.RootScreen

class MainActivity : AppCompatActivity(), NavigationListener {

  private val rootScreen = RootScreen()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    app(this).injector().inject(this)
    setContentScreen(rootScreen, R.id.magellan_container)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    rootScreen.updateMenu(menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    rootScreen.updateMenu(menu)
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onBackPressed() {
    if (!rootScreen.backPressed()) {
      super.onBackPressed()
    }
  }

  override fun onNavigate(actionBarConfig: ActionBarConfig) {
    invalidateOptionsMenu()
  }
}