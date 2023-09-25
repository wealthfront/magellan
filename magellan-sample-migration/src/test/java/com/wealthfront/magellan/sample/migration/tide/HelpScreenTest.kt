package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.TestSampleApplication
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogMessage
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(application = TestSampleApplication::class)
class HelpScreenTest {

  @Mock lateinit var helpView: HelpView

  private lateinit var helpScreen: HelpScreen
  private lateinit var context: Context
  private var goToBreedsStep = false

  @Inject lateinit var api: DogApi

  @Before
  fun setup() {
    initMocks(this)
    context = ApplicationProvider.getApplicationContext()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    helpScreen = object : HelpScreen({ goToBreedsStep = true }) {
      override fun createView(context: Context): HelpView {
        return helpView
      }
    }

    `when`(api.getRandomImageForBreed("husky")).thenReturn(
      Observable.just(
        DogMessage(
          message = "https://dailybeagle.com/latest-picture",
          status = "something"
        )
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
