package com.wealthfront.magellan.uitest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.wealthfront.magellan.assertions.assertShown
import com.wealthfront.magellan.sample.MainActivity
import com.wealthfront.magellan.sample.R
import org.junit.Rule
import org.junit.Test

class DialogTest {

  @get:Rule
  var activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun showDialog() {
    onView(withId(R.id.nextJourney)).perform(click())

    onView(withId(R.id.dialog)).perform(click())
    assertShown { text("Hello") }
    assertShown { text("Are you sure about this?") }
  }
}
