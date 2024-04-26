package com.wealthfront.magellan.sample.migration.tide

import android.app.Application
import android.os.Looper.getMainLooper
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogBreedsResponse
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DogListStepTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var dogListStep: DogListStep
  private val activityController = Robolectric.buildActivity(ComponentActivity::class.java)

  private var chosenBreed: String? = null

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val component = ((context as AppComponentContainer).injector() as TestAppComponent)
    dogListStep = component.dogListStepFactory.create { chosenBreed = it }
    coEvery { component.api.getAllBreeds() } returns
      DogBreedsResponse(message = mapOf("akita" to emptyList()), status = "success")
  }

  @Test
  fun goesToSelectedDogBreed() {
    activityController.get().setContentScreen(dogListStep)
    activityController.setup()
    shadowOf(getMainLooper()).idle()

    composeTestRule.onNodeWithTag("DogBreeds").onChildAt(0).performClick()
    assertThat(chosenBreed).isEqualTo("akita")
  }
}
