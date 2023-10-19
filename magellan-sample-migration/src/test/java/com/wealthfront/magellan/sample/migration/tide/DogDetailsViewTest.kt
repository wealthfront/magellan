package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DogDetailsViewTest {

  @Mock lateinit var glideRequest: RequestManager
  @Mock lateinit var drawableRequest: RequestBuilder<Drawable>

  private lateinit var view: DogDetailsView
  private lateinit var context: Context

  @Rule @JvmField
  val mockitoRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.WARN)

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    view = DogDetailsView(context).apply {
      glideBuilder = glideRequest
    }

    `when`(glideRequest.load(anyString())).thenReturn(drawableRequest)
  }

  @Test
  fun fetchesDogPicOnShow() {
    view.setDogPic("https://dailybeagle.com/latest-picture")
    shadowOf(getMainLooper()).idle()
    verify(drawableRequest).into(view.viewBinding.dogImage)
  }
}
