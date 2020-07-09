package com.wealthfront.magellan.compose.lifecycle

import android.app.Activity
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wealthfront.magellan.compose.core.Screen

fun ComponentActivity.setContentScreen(
  screen: Screen,
  @IdRes containerRes: Int
) {
  lifecycle.addObserver(ActivityLifecycleAdapter(screen, this, containerRes))
}

internal class ActivityLifecycleAdapter(
  private val screen: Screen,
  private val context: Activity,
  private val containerRes: Int
) : DefaultLifecycleObserver {

  override fun onCreate(owner: LifecycleOwner) {
    screen.create(context)
  }

  override fun onStart(owner: LifecycleOwner) {
    screen.show(context)
    context.findViewById<FrameLayout>(containerRes).addView(screen.view!!)
  }

  override fun onResume(owner: LifecycleOwner) {
    screen.resume(context)
  }

  override fun onPause(owner: LifecycleOwner) {
    screen.pause(context)
  }

  override fun onStop(owner: LifecycleOwner) {
    screen.hide(context)
    context.findViewById<FrameLayout>(containerRes).removeAllViews()
  }

  override fun onDestroy(owner: LifecycleOwner) {
    if (context.isFinishing) {
      screen.destroy(context)
    }
  }
}
