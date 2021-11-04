package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.wealthfront.magellan.sample.migration.TestSampleApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestSampleApplication::class)
class HelpViewTest {

  @Mock lateinit var screen: HelpScreen
  @Mock lateinit var glideRequest: RequestManager
  @Mock lateinit var drawableRequest: RequestBuilder<Drawable>

  private lateinit var helpView: HelpView
  private lateinit var context: Context

  @Before
  fun setup() {
    initMocks(this)
    context = ApplicationProvider.getApplicationContext()
    helpView = HelpView(context, screen).apply {
      glideBuilder = glideRequest
    }

    `when`(glideRequest.load(anyString())).thenReturn(drawableRequest)
  }

  @Test
  fun fetchesDogPicOnShow() {
    helpView.setDogPic("https://dailybeagle.com/latest-picture")
    shadowOf(getMainLooper()).idle()
    verify(drawableRequest).into(helpView.binding.dogImage)
  }
}
