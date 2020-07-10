package com.wealthfront.magellan.lifecycle

import android.app.Activity
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.lifecycle.DefaultLifecycleObserver
import com.wealthfront.magellan.core.Navigable
import androidx.lifecycle.LifecycleOwner
import com.wealthfront.magellan.ScreenContainer

internal class ActivityLifecycleAdapter(
  private val navigable: Navigable,
  private val context: Activity,
  private val containerRes: Int
) : DefaultLifecycleObserver {

  override fun onCreate(owner: LifecycleOwner) {
    navigable.create(context)
  }

  override fun onStart(owner: LifecycleOwner) {
    navigable.show(context)
    context.findViewById<ScreenContainer>(containerRes).addView(navigable.view!!)
  }

  override fun onResume(owner: LifecycleOwner) {
    navigable.resume(context)
  }

  override fun onPause(owner: LifecycleOwner) {
    navigable.pause(context)
  }

  override fun onStop(owner: LifecycleOwner) {
    navigable.hide(context)
    context.findViewById<ScreenContainer>(containerRes).removeAllViews()
  }

  override fun onDestroy(owner: LifecycleOwner) {
    if (context.isFinishing) {
      navigable.destroy(context)
    }
  }
}

fun ComponentActivity.setContentScreen(
  navigable: Navigable,
  @IdRes containerRes: Int
) {
  lifecycle.addObserver(ActivityLifecycleAdapter(navigable, this, containerRes))
}