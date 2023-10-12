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
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogImageResponse
import com.wealthfront.magellan.sample.migration.coWhen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DogDetailsScreenTest {
  private lateinit var screen: DogDetailsScreen
  private val activity = buildActivity(ComponentActivity::class.java).get()
  private val breedData = DogImageResponse(
    message = "image-url",
    status = "success"
  )

  @Inject lateinit var api: DogApi
  @Mock lateinit var dogDetailsView: DogDetailsView

  @Rule @JvmField
  val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Application>()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    screen = object : DogDetailsScreen("robotic") {
      override fun createView(context: Context): DogDetailsView {
        super.createView(context)
        return dogDetailsView
      }
    }

    coWhen { api.getRandomImageForBreed("robotic") }.thenReturn(breedData)
  }

  @Test
  fun fetchesDogBreedOnShow() {
    screen.transitionToState(LifecycleState.Shown(activity))
    shadowOf(getMainLooper()).idle()
    verify(dogDetailsView).setDogPic("image-url")
  }
}
