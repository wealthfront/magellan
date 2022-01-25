package com.wealthfront.magellan.uitest

import androidx.lifecycle.Lifecycle.State
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wealthfront.magellan.action.orientationLandscape
import com.wealthfront.magellan.onDialogView
import com.wealthfront.magellan.sample.MainActivity
import com.wealthfront.magellan.sample.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DialogTest {

  @get:Rule
  var activityRule = ActivityScenarioRule(MainActivity::class.java)

  @Before
  fun setup() {
    activityRule.scenario.moveToState(State.RESUMED)
    onView(withId(R.id.nextJourney)).perform(click())
  }

  @Test
  fun showDialog_pressBack() {
    onView(withId(R.id.dialog1)).perform(click())
    assertDialogContentsShown()

    pressBack()

    assertDialogContentsNotShown()
  }

  @Test
  fun showDialog_rotation() {
    onView(withId(R.id.dialog1)).perform(click())
    assertDialogContentsShown()

    onView(isRoot()).perform(orientationLandscape())

    assertDialogContentsShown()
  }

  @Test
  fun showDialog_resume() {
    onView(withId(R.id.dialog1)).perform(click())
    assertDialogContentsShown()

    activityRule.scenario.recreate()

    assertDialogContentsShown()
  }
}

private fun assertDialogContentsShown() {
  setOf("Hello", "Are you sure about this?").forEach { message ->
    onDialogView(withText(message)).check(matches(isDisplayed()))
  }
}

private fun assertDialogContentsNotShown() {
  onView(withText("Hello")).check(doesNotExist())
}