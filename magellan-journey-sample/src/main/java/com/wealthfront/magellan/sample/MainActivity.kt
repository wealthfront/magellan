package com.wealthfront.magellan.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.App.Provider.appComponent
import com.wealthfront.magellan.view.MenuComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var expedition: Expedition
  @Inject lateinit var menuComponent: MenuComponent

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
    menuComponent.setMenu(menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    menuComponent.setMenu(menu)
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
}
