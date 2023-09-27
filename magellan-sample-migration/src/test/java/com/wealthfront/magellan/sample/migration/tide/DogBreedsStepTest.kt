package com.wealthfront.magellan.sample.migration.tide

import android.app.Activity
import android.app.Application
import android.os.Looper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogBreeds
import com.wealthfront.magellan.sample.migration.coWhen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DogBreedsStepTest {

  private val dogBreedsStep = DogBreedsStep()
  private val activity = Robolectric.buildActivity(Activity::class.java).get()

  @Inject lateinit var api: DogApi

  @Rule @JvmField
  val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

  @Before
  fun setup() {
    val context = getApplicationContext<Application>()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    val breedData = DogBreeds(
      message = listOf("chesapeake", "curly", "flatcoated", "golden"),
      status = "success"
    )
    coWhen { api.getListOfAllBreedsOfRetriever() }.thenReturn(breedData)
  }

  @Test
  fun fetchesDogBreedsOnShow() {
    dogBreedsStep.transitionToState(LifecycleState.Shown(activity))
    shadowOf(Looper.getMainLooper()).idle()
    assertThat(dogBreedsStep.viewBinding!!.dogBreeds.adapter).isNotNull()
  }
}
