package com.wealthfront.magellan.uitest

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.wealthfront.magellan.sample.migration.MainActivity
import com.wealthfront.magellan.sample.migration.R
import org.junit.Before
import org.junit.Test

class NavigationTest {

  lateinit var activityScenario: ActivityScenario<MainActivity>

  @Before
  fun setup() {
    activityScenario = launchActivity()
  }

  @Test
  fun visitRetriever() {
    onView(withText("Akita")).perform(click())
    onView(withId(R.id.dogDetailsView)).check(matches(isDisplayed()))
  }
}
