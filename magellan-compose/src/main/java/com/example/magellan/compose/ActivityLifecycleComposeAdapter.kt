package com.example.magellan.compose

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.DefaultLifecycleObserver
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import com.wealthfront.magellan.lifecycle.LifecycleState

public class ActivityLifecycleComposeAdapter(
  private val navigable: Navigable<@Composable () -> Unit>,
  private val context: Activity
) : DefaultLifecycleObserver {

  override fun onStart(owner: androidx.lifecycle.LifecycleOwner) {
    navigable.show(context)
  }

  override fun onResume(owner: androidx.lifecycle.LifecycleOwner) {
    navigable.resume(context)
  }

  override fun onPause(owner: androidx.lifecycle.LifecycleOwner) {
    navigable.pause(context)
  }

  override fun onStop(owner: androidx.lifecycle.LifecycleOwner) {
    navigable.hide(context)
  }

  override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
    if (context.isFinishing) {
      navigable.destroy(context.applicationContext)
    }
  }
}

public fun ComponentActivity.setContentNavigable(navigable: Navigable<@Composable () -> Unit>) {
  setContent { navigable.view!!() }
  if (navigable is LifecycleOwner && navigable.currentState == LifecycleState.Destroyed) {
    navigable.create(applicationContext)
  }
  lifecycle.addObserver(ActivityLifecycleComposeAdapter(navigable, this))
}
