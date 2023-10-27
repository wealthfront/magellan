package com.wealthfront.magellan.sample.migration.tide

import android.app.Application
import android.os.Looper.getMainLooper
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.setContentScreen
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogBreedsResponse
import io.mockk.coEvery
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DogListStepTest {

  private lateinit var dogListStep: DogListStep
  @Inject lateinit var dogListStepFactory: DogListStepFactory
  private val activityController = Robolectric.buildActivity(ComponentActivity::class.java)

  private var chosenBreed: String? = null

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val component = ((context as AppComponentContainer).injector() as TestAppComponent)
    component.inject(this)
    dogListStep = dogListStepFactory.create { chosenBreed = it }
    coEvery { component.api.getAllBreeds() } returns
      DogBreedsResponse(message = mapOf("akita" to emptyList()), status = "success")
  }

  @Test
  fun goesToSelectedDogBreed() {
    activityController.get().setContentScreen(dogListStep)
    activityController.setup()
    shadowOf(getMainLooper()).idle()

    dogListStep.viewBinding!!.dogItems.findViewHolderForAdapterPosition(0)!!.itemView.performClick()
    assertThat(chosenBreed).isEqualTo("akita")
  }
}
