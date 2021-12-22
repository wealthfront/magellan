package com.wealthfront.magellan

import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers.hasWindowLayoutParams
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.isFocusable
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf

/** Modified default root matcher at {@link androidx.test.espresso.matcher.RootMatchers.DEFAULT} */
internal val activityRootMatcher: Matcher<Root> = allOf(
  hasWindowLayoutParams(),
  allOf(
    IsSubwindowOfCurrentActivity(),
    isFocusable()
  )
)

/** Modified default root matcher at {@link androidx.test.espresso.matcher.RootMatchers.DEFAULT} */
internal val dialogRootMatcher: Matcher<Root> = allOf(
  hasWindowLayoutParams(),
  allOf(
    allOf(isDialog(), withDecorView(HasWindowFocus())),
    isFocusable()
  )
)

/** Copied from {@link androidx.test.espresso.matcher.RootMatchers.IsSubwindowOfCurrentActivity} */
private class IsSubwindowOfCurrentActivity : TypeSafeMatcher<Root>() {

  override fun describeTo(description: Description) {
    description.appendText("is subwindow of current activity")
  }

  public override fun matchesSafely(root: Root): Boolean {
    return getResumedActivityTokens().contains(root.decorView.applicationWindowToken)
  }
}

/** Copied from {@link androidx.test.espresso.matcher.RootMatchers.HasWindowFocus} */
private class HasWindowFocus : TypeSafeMatcher<View>() {

  override fun describeTo(description: Description) {
    description.appendText("has window focus")
  }

  public override fun matchesSafely(view: View): Boolean {
    return view.hasWindowFocus()
  }
}

/** Copied from {@link androidx.test.espresso.matcher.RootMatchers#getResumedActivityTokens()} */
private fun getResumedActivityTokens(): List<IBinder> {
  val activityLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance()
  val resumedActivities = activityLifecycleMonitor.getActivitiesInStage(Stage.RESUMED)
  if (resumedActivities.isEmpty()) {
    Log.e("WealthfrontRootMatchers", "At least one activity should be in RESUMED stage.")
  }
  val tokens: MutableList<IBinder> = arrayListOf()
  for (activity in resumedActivities) {
    tokens.add(activity.window.decorView.applicationWindowToken)
  }
  return tokens
}
