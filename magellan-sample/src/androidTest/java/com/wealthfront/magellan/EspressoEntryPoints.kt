package com.wealthfront.magellan

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import org.hamcrest.Matcher

fun onDialogView(viewMatcher: Matcher<View>): ViewInteraction {
  return Espresso.onView(viewMatcher).inRoot(dialogRootMatcher)
}
