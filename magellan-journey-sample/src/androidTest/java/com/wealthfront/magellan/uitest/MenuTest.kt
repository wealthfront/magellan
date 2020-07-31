package com.wealthfront.magellan.uitest

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.wealthfront.magellan.assertions.assertShown
import com.wealthfront.magellan.sample.MainActivity
import com.wealthfront.magellan.sample.R
import org.junit.Rule
import org.junit.Test

class MenuTest {

  @get:Rule
  var activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun navigationTest() {
    onView(withId(R.id.nextJourney)).perform(click())

    onView(withId(R.id.notifications)).perform(click())
    assertShown { text(R.string.learn_more_text) }
    pressBack()

    activityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    onView(withId(R.id.notifications)).check(matches(isDisplayed()))
  }
}
