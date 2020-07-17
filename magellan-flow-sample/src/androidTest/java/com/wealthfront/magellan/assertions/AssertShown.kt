package com.wealthfront.magellan.assertions

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.util.HumanReadables
import kotlin.reflect.KClass
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher

fun assertShown(action: AssertionBuilder.() -> Unit) {
  AssertionBuilder(::scrollToAndAssertVisible).action()
}

fun assertNotShown(action: AssertionBuilder.() -> Unit) {
  AssertionBuilder(::assertNotVisible).action()
}

class AssertionBuilder(private val verify: (viewMatcher: Matcher<View>) -> Unit) {
  fun text(@StringRes textRes: Int) {
    verify(withText(textRes))
  }

  fun text(text: String) {
    verify(withText(text))
  }

  fun someText(text: String) {
    verify(withText(containsString(text)))
  }

  fun view(clazz: KClass<out View>) {
    verify(withClassName(equalTo(clazz.java.name)))
  }
}

private fun scrollToAndAssertVisible(viewMatcher: Matcher<View>) {
  onView(allOf(viewMatcher, withEffectiveVisibility(Visibility.VISIBLE)))
    .check(matches(isDisplayed()))
}

private fun assertNotVisible(viewMatcher: Matcher<View>) {
  onView(viewMatcher)
    .check(isNotShown)
}

private val isNotShown = ViewAssertion { view, _ ->
  if (view != null && withEffectiveVisibility(Visibility.VISIBLE).matches(view)) {
    throw AssertionError("View is present and visible in the hierarchy: " +
      HumanReadables.describe(view))
  }
}
