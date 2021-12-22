package com.wealthfront.magellan.uitest

import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.wealthfront.magellan.assertions.assertShown
import com.wealthfront.magellan.onView
import com.wealthfront.magellan.sample.MainActivity
import com.wealthfront.magellan.sample.R
import org.junit.Rule
import org.junit.Test

class NavigationTest {

  @get:Rule
  var activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun navigationTest() {
    runThroughTheAppAndCheckForProperNavigation()
  }

  @Test
  fun properResumption() {
    assertShown { text(R.string.intro_view_text) }
    onView(withId(R.id.learnMore)).perform(click())
    assertShown { text(R.string.learn_more_text) }

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.pressHome()

    reopenApp()

    assertShown { text(R.string.learn_more_text) }
    pressBack()
    assertShown { text(R.string.intro_view_text) }
    pressBackUnconditionally()
  }

  private fun runThroughTheAppAndCheckForProperNavigation() {
    assertShown { text(R.string.intro_view_text) }
    onView(withId(R.id.learnMore)).perform(click())
    assertShown { text(R.string.learn_more_text) }
    onView(withId(R.id.nextJourney)).perform(click())
    assertShown { text(R.string.detail_memo_text) }
    pressBack()
    assertShown { text(R.string.learn_more_text) }
    pressBack()
    assertShown { text(R.string.intro_view_text) }
    onView(withId(R.id.nextJourney)).perform(click())
    assertShown { text(R.string.detail_memo_text) }
    pressBack()
    assertShown { text(R.string.intro_view_text) }
    pressBackUnconditionally()
    assert(activityRule.activity.isDestroyed)
  }

  private fun reopenApp() {
    val intent = Intent(activityRule.activity, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    activityRule.activity.startActivity(intent)
  }
}
