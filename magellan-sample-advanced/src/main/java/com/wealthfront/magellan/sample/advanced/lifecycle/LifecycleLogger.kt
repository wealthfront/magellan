package com.wealthfront.magellan.sample.advanced.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.wealthfront.magellan.lifecycle.LifecycleListener
import javax.inject.Inject

private const val TAG = "LifecycleLogger"

class LifecycleLogger @Inject constructor() : LifecycleListener {

  override fun onShow(context: Context) {
    Log.d(TAG, "onShow called")
  }

  override fun onResume(context: Context) {
    Log.d(TAG, "onResume called")
  }

  override fun onPause(context: Context) {
    Log.d(TAG, "onPause called")
  }

  override fun onHide(context: Context) {
    Log.d(TAG, "onHide called")
  }

  override fun onSave(outState: Bundle) {
    Log.d(TAG, "onSave called")
  }

  override fun onRestore(savedInstanceState: Bundle) {
    Log.d(TAG, "onRestore called")
  }
}