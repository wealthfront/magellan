package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.TestAppComponent
import com.wealthfront.magellan.sample.migration.TestSampleApplication
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.api.DogMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import rx.Observable
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(application = TestSampleApplication::class)
class HelpScreenTest {

  @Mock lateinit var view: HelpView
  @Mock lateinit var glideRequest: RequestManager
  @Mock lateinit var drawableRequest: RequestBuilder<Drawable>

  private lateinit var helpScreen: HelpScreen
  private lateinit var context: Context

  @Inject lateinit var api: DogApi

  @Before
  fun setup() {
    initMocks(this)
    helpScreen = HelpScreen { }
    context = ApplicationProvider.getApplicationContext()
    ((context as AppComponentContainer).injector() as TestAppComponent).inject(this)

    `when`(glideRequest.load(anyString())).thenReturn(drawableRequest)
    view.glideBuilder = glideRequest
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
    helpScreen.transitionToState(LifecycleState.Created(context))
    helpScreen.view = view
    helpScreen.transitionToState(LifecycleState.Shown(context))
    shadowOf(getMainLooper()).idle()
    verify(view).setDogPic("https://dailybeagle.com/latest-picture")
  }
}
