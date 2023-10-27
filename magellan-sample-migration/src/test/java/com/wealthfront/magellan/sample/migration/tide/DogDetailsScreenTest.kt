package com.wealthfront.magellan.sample.migration.tide

import android.app.Application
import android.content.Context
import android.os.Looper.getMainLooper
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogImageResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DogDetailsScreenTest {
  private lateinit var screen: DogDetailsScreen
  private val activity = buildActivity(ComponentActivity::class.java).get()
  private val breedData = DogImageResponse(
    message = "image-url",
    status = "success"
  )

  private val dogDetailsView = mockk<DogDetailsView>(relaxed = true)

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val component = ((context as AppComponentContainer).injector() as TestAppComponent)

    screen = object : DogDetailsScreen(
      component.api,
      component.toolbarHelper,
      "robotic"
    ) {
      override fun createView(context: Context): DogDetailsView {
        super.createView(context)
        return dogDetailsView
      }
    }

    coEvery { component.api.getRandomImageForBreed("robotic") } returns breedData
  }

  @Test
  fun fetchesDogBreedOnShow() {
    screen.transitionToState(LifecycleState.Shown(activity))
    shadowOf(getMainLooper()).idle()
    coVerify { dogDetailsView.setDogPic("image-url") }
  }
}
