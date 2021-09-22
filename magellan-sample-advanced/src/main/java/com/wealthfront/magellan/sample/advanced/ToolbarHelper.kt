package com.wealthfront.magellan.sample.advanced

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

object ToolbarHelperProvider {

  var toolbarHelper = ToolbarHelper()
}

class ToolbarHelper : LifecycleEventObserver {

  private var toolbar: Toolbar? = null
  private var actionBar: ActionBar? = null
  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    if (event == Lifecycle.Event.ON_CREATE) {
      toolbar = (source as MainActivity).findViewById(R.id.toolbar)
      source.setSupportActionBar(toolbar)
      this.actionBar = source.supportActionBar

      actionBar!!.setDisplayHomeAsUpEnabled(true)
      actionBar!!.setDisplayShowTitleEnabled(true)
    } else if (event == Lifecycle.Event.ON_DESTROY) {
      actionBar = null
    }
  }

  fun setTitle(title: String) {
    actionBar!!.title = title
  }

  fun showToolbar() {
    toolbar!!.visibility = VISIBLE
  }

  fun hideToolbar() {
    toolbar!!.visibility = GONE
  }
}
