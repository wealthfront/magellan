package com.wealthfront.magellan.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.App.Provider.appComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  var menu: Menu? = null

  @Inject lateinit var expedition: Expedition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appComponent.inject(this)
    setContentScreen(expedition)
    initActionBar()
  }

  private fun initActionBar() {
    val actionBar = super.getSupportActionBar()!!
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    this.menu = menu
    updateMenu()
    return super.onCreateOptionsMenu(menu)
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    this.menu = menu
    updateMenu()
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onBackPressed() {
    if (!expedition.backPressed()) {
      super.onBackPressed()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun updateMenu() {
    if (menu != null) {
      for (i in 0 until menu!!.size()) {
        menu!!.getItem(i).isVisible = false
      }
    }
  }
}
