package com.wealthfront.magellan.sample.migration.uitest

import android.app.Application
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.CoroutineIdlingRule
import com.wealthfront.magellan.sample.migration.DisableAnimationsAndKeyboardRule
import com.wealthfront.magellan.sample.migration.MainActivity
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogBreedsResponse
import com.wealthfront.magellan.sample.migration.api.DogImageResponse
import com.wealthfront.magellan.sample.migration.coWhen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class NavigationTest {

  @Rule @JvmField
  val disableAnimationsAndKeyboardRule = DisableAnimationsAndKeyboardRule()

  @Rule @JvmField
  val coroutineIdlingRule = CoroutineIdlingRule()

  @Rule @JvmField
  val composeRule: ComposeTestRule = createEmptyComposeRule()

  @Inject lateinit var api: DogApi

  private lateinit var activityScenario: ActivityScenario<MainActivity>

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    coWhen { api.getAllBreeds() }
      .thenReturn(DogBreedsResponse(message = mapOf("robotic" to emptyList()), status = "success"))
    coWhen { api.getRandomImageForBreed("robotic") }.thenReturn(
      DogImageResponse(message = "image-url", status = "success")
    )
  }

  @Test
  fun visitRetriever() {
    activityScenario = launchActivity()

    composeRule.onNodeWithText("robotic").performClick()
  }
}
