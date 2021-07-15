package com.wealthfront.magellan.lifecycle

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle as ActivityLifecycle
import androidx.lifecycle.LifecycleOwner as ActivityLifecycleOwner
import com.wealthfront.magellan.R
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed

private var adapterMap = emptyMap<Navigable, Pair<ActivityLifecycleAdapter, ActivityLifecycle>>()

internal class ActivityLifecycleAdapter(
  private val navigable: Navigable,
  private val context: Activity
) : DefaultLifecycleObserver {

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
    navigable.detachAndRemoveFromStaticMap(context.applicationContext)
    if (context.isFinishing) {
      navigable.destroy(context.applicationContext)
    }
  }
}

public fun ComponentActivity.setContentScreen(
  navigable: Navigable,
  @LayoutRes root: Int = R.layout.magellan_root
) {
  setContentView(root)
  @Suppress("DEPRECATION") setExpedition(navigable)
}

@Deprecated(
  "This method exists for migration purposes.",
  ReplaceWith("setContentScreen(navigable)"))
public fun ComponentActivity.setExpedition(navigable: Navigable) {
  if (navigable is LifecycleOwner && navigable.currentState == Destroyed) {
    navigable.create(applicationContext)
  }
  if (adapterMap.containsKey(navigable)) {
    navigable.detachAndRemoveFromStaticMap(applicationContext)
  }
  val lifecycleAdapter = ActivityLifecycleAdapter(navigable, this)
  navigable.attachAndAddToStaticMap(lifecycleAdapter, lifecycle)
}

private fun Navigable.attachAndAddToStaticMap(
  lifecycleAdapter: ActivityLifecycleAdapter,
  lifecycle: ActivityLifecycle
) {
  lifecycle.addObserver(lifecycleAdapter)
  adapterMap = adapterMap + (this to (lifecycleAdapter to lifecycle))
}

private fun Navigable.detachAndRemoveFromStaticMap(applicationContext: Context) {
  val (lifecycleAdapter, lifecycle) = adapterMap[this]!!
  lifecycle.removeObserver(lifecycleAdapter)
  if (this is LifecycleOwner && currentState !is Created) {
    LifecycleStateMachine().transition(this, currentState, Created(applicationContext))
  }
  adapterMap = adapterMap - this
}
