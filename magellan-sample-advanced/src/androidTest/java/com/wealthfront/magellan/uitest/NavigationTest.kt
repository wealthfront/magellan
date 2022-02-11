package com.wealthfront.magellan.uitest

import androidx.appcompat.widget.AppCompatButton
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.wealthfront.magellan.sample.advanced.MainActivity
import com.wealthfront.magellan.sample.advanced.R
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test

class NavigationTest {

  lateinit var activityScenario: ActivityScenario<MainActivity>

  @Before
  fun setup() {
    activityScenario = launchActivity()
    onView(withText("Welcome to the Cereal Museum!")).check(matches(isDisplayed()))
  }

  @Test
  fun buildYourOwn() {
    onView(withId(R.id.designCereal)).perform(click())
    onView(withText("Design a cereal!")).check(matches(isDisplayed()))
    onView(withText("Corn Flakes")).perform(click())

    onView(withText("Design a cereal!")).check(matches(isDisplayed()))
    onView(withText("Next, choose a style")).check(matches(isDisplayed()))
    onView(withText("Plain")).perform(click())
    onView(withText("What color should it be?")).check(matches(isDisplayed()))
    onView(withText("Rainbow")).perform(click())
    onView(withId(R.id.next)).perform(click())

    onView(withText("Sounds delicious! You made:")).check(matches(isDisplayed()))
    onView(withText("Rainbow")).check(matches(isDisplayed()))
    onView(withText("Plain")).check(matches(isDisplayed()))
    onView(withText("Corn Flakes")).check(matches(isDisplayed()))

    activityScenario.recreate()

    onView(withText("Sounds delicious! You made:")).check(matches(isDisplayed()))
  }

  @Test
  fun browseCollection() {
    onView(withId(R.id.browseCollection)).perform(click())

    onView(allOf(withText(R.string.monster_cereals_title), withId(R.id.cerealTitle))).check(
      matches(
        isDisplayed()))
    onView(
      allOf(
        withText(R.string.cornflakes_title),
        isAssignableFrom(AppCompatButton::class.java))).perform(click())
    onView(allOf(withText(R.string.cornflakes_title), withId(R.id.cerealTitle))).check(
      matches(
        isDisplayed()))

    pressBack()
    onView(withText("Welcome to the Cereal Museum!")).check(matches(isDisplayed()))
  }

  @Test
  fun orderTickets() {
    onView(withId(R.id.orderTickets)).perform(click())

    onView(withId(R.id.adultTicketCount)).perform(replaceText("2"))
    onView(withId(R.id.childTicketCount)).perform(replaceText("3"))
    onView(withText("Your total: $32.00")).check(matches(isDisplayed()))
    onView(withId(R.id.next)).perform(click())

    onView(withText("Add new credit card")).perform(click())
    onView(withId(R.id.creditCardNumber)).perform(replaceText("0000-1111-2222-3333"))
    onView(withId(R.id.done)).perform(click())

    onView(withText("2 Adult tickets\n3 Child tickets")).check(matches(isDisplayed()))
    onView(withText("Credit card 0000-1111-2222-3333")).check(matches(isDisplayed()))
    onView(withId(R.id.order)).perform(click())

    onView(withId(R.id.done)).perform(click())
    onView(withText("Welcome to the Cereal Museum!")).check(matches(isDisplayed()))
  }

  @Test
  fun suggestExhibit() {
    onView(withId(R.id.suggestExhibit)).perform(click())
    onView(withText("Download the latest version")).check(matches(isDisplayed()))
    pressBack()
    onView(withText("Welcome to the Cereal Museum!")).check(matches(isDisplayed()))
  }
}
