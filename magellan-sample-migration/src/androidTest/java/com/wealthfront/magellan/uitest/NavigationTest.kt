package com.wealthfront.magellan.uitest

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.wealthfront.magellan.sample.migration.MainActivity
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

  }
}
