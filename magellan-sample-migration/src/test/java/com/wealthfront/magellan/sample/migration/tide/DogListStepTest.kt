package com.wealthfront.magellan.sample.migration.tide

import android.os.Looper.getMainLooper
import androidx.activity.ComponentActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.setContentScreen
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DogListStepTest {
  private var chosenBreed: String? = null
  private val dogListStep = DogListStep { chosenBreed = it }
  private val activityController = Robolectric.buildActivity(ComponentActivity::class.java)

  @Test
  fun goesToSelectedDogBreed() {
    activityController.get().setContentScreen(dogListStep)
    activityController.setup()
    shadowOf(getMainLooper()).idle()

    dogListStep.viewBinding!!.dogItems.getChildAt(0).performClick()
    assertThat(chosenBreed).isEqualTo("akita")
  }
}
