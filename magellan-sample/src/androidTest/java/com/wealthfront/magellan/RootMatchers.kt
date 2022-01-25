package com.wealthfront.magellan

import android.view.View
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers.hasWindowLayoutParams
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.isFocusable
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf

/** Modified default root matcher at {@link androidx.test.espresso.matcher.RootMatchers.DEFAULT} */
internal val dialogRootMatcher: Matcher<Root> = allOf(
  hasWindowLayoutParams(),
  allOf(
    allOf(isDialog(), withDecorView(HasWindowFocus())),
    isFocusable()
  )
)

/** Copied from {@link androidx.test.espresso.matcher.RootMatchers.HasWindowFocus} */
private class HasWindowFocus : TypeSafeMatcher<View>() {

  override fun describeTo(description: Description) {
    description.appendText("has window focus")
  }

  public override fun matchesSafely(view: View): Boolean {
    return view.hasWindowFocus()
  }
}
