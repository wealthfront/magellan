package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class DogDetailsViewTest {

  private val glideRequest = mockk<RequestManager>(relaxed = true)
  private val drawableRequest = mockk<RequestBuilder<Drawable>>(relaxed = true)

  private lateinit var view: DogDetailsView
  private lateinit var context: Context

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    view = DogDetailsView(context).apply {
      glideBuilder = glideRequest
    }

    every { glideRequest.load(ofType(String::class)) } returns drawableRequest
  }

  @Test
  fun fetchesDogPicOnShow() {
    view.setDogPic("https://dailybeagle.com/latest-picture")
    shadowOf(getMainLooper()).idle()
    verify { drawableRequest.into(view.viewBinding.dogImage) }
  }
}
