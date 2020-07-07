package com.wealthfront.magellan.lifecycle

import android.content.Context
import android.os.Bundle

interface LifecycleListener {

  fun onShow(context: Context)

  fun onResume(context: Context)

  fun onPause(context: Context)

  fun onHide(context: Context)

  fun onSave(outState: Bundle)

  fun onRestore(savedInstanceState: Bundle)

}