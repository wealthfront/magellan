package com.wealthfront.magellan.action

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matcher

class OrientationChangeAction(private val orientation: Int) : ViewAction {

  override fun getDescription(): String = "change orientation to $orientation"

  override fun getConstraints(): Matcher<View> = isRoot()

  override fun perform(uiController: UiController, view: View) {
    uiController.loopMainThreadUntilIdle()
    val activity = getActivity(view.context)!!
    activity.requestedOrientation = orientation
    val resumedActivities =
      ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
    if (resumedActivities.isEmpty()) {
      throw RuntimeException("Could not change orientation to $orientation")
    }
  }
}

private fun getActivity(context: Context): Activity? {
  if (context is Activity) {
    return context
  }
  if (context is ContextWrapper) {
    return getActivity(context.baseContext)
  }
  return null
}

fun orientationLandscape(): OrientationChangeAction {
  return OrientationChangeAction(SCREEN_ORIENTATION_LANDSCAPE)
}

fun orientationPortrait(): OrientationChangeAction {
  return OrientationChangeAction(SCREEN_ORIENTATION_PORTRAIT)
}
