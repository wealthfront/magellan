package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogMessage
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class HelpScreenTest {

  @Mock lateinit var helpView: HelpView

  private lateinit var helpScreen: HelpScreen
  private lateinit var context: Context
  private var goToBreedsStep = false

  @Inject lateinit var api: DogApi

  @Rule @JvmField
  val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    helpScreen = object : HelpScreen({ goToBreedsStep = true }) {
      override fun createView(context: Context): HelpView {
        super.createView(context)
        return helpView
      }
    }

    coWhen { api.getRandomImageForBreed("husky") }.thenReturn(
      DogMessage(
        message = "https://dailybeagle.com/latest-picture",
        status = "something"
      )
    )
  }

  @Test
  fun fetchesDogPicOnShow() {
    helpScreen.transitionToState(LifecycleState.Shown(context))
    shadowOf(getMainLooper()).idle()
    verify(helpView).setDogPic("https://dailybeagle.com/latest-picture")
  }
}
