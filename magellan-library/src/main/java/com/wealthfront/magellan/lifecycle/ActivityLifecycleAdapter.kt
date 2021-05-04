package com.wealthfront.magellan.lifecycle

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.lifecycle.DefaultLifecycleObserver
import com.wealthfront.magellan.R
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import androidx.lifecycle.LifecycleOwner as ActivityLifecycleOwner
import com.wealthfront.magellan.lifecycle.LifecycleOwner as MagellanLifecycleOwner

internal class ActivityLifecycleAdapter(
  private val navigable: Navigable<View>,
  private val context: Activity
) : DefaultLifecycleObserver {

  override fun onCreate(owner: ActivityLifecycleOwner) {
    if (navigable is MagellanLifecycleOwner && navigable.currentState == LifecycleState.Destroyed) {
      navigable.create(context.applicationContext)
    }
  }

  override fun onStart(owner: ActivityLifecycleOwner) {
    navigable.show(context)
    context.findViewById<ScreenContainer>(R.id.magellan_container).addView(navigable.view!!)
  }

  override fun onResume(owner: ActivityLifecycleOwner) {
    navigable.resume(context)
  }

  override fun onPause(owner: ActivityLifecycleOwner) {
    navigable.pause(context)
  }

  override fun onStop(owner: ActivityLifecycleOwner) {
    navigable.hide(context)
    context.findViewById<ScreenContainer>(R.id.magellan_container).removeAllViews()
  }

  override fun onDestroy(owner: ActivityLifecycleOwner) {
    if (context.isFinishing) {
      navigable.destroy(context.applicationContext)
    }
  }
}

public fun ComponentActivity.setContentScreen(navigable: Navigable<View>, @LayoutRes root: Int = R.layout.magellan_root) {
  setContentView(root)
  lifecycle.addObserver(ActivityLifecycleAdapter(navigable, this))
}

@Deprecated("This method exists for migration purposes.", ReplaceWith("setContentScreen(navigable)"))
public fun ComponentActivity.setExpedition(navigable: Navigable<View>) {
  lifecycle.addObserver(ActivityLifecycleAdapter(navigable, this))
}
