package com.ryanmoelter.magellanx.core.lifecycle

import android.widget.FrameLayout
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

public class CreateAndAttachFieldToLifecycleWhenShownDelegateTest {

  private lateinit var lifecycleView: CreateAndAttachFieldToLifecycleWhenShownDelegate<FrameLayout>

  @Mock
  private lateinit var frameLayout: FrameLayout

  private lateinit var mockSession: AutoCloseable

  @Before
  public fun setUp() {
    mockSession = MockitoAnnotations.openMocks(this)
    lifecycleView = CreateAndAttachFieldToLifecycleWhenShownDelegate { frameLayout }
  }

  @After
  public fun tearDown() {
    mockSession.close()
  }

  @Test
  public fun wholeLifecycle() {
    lifecycleView.create()
    lifecycleView.field.shouldBeNull()

    lifecycleView.show()
    lifecycleView.field shouldBe frameLayout

    lifecycleView.resume()
    lifecycleView.field shouldBe frameLayout

    lifecycleView.pause()
    lifecycleView.field shouldBe frameLayout

    lifecycleView.hide()
    lifecycleView.field.shouldBeNull()

    lifecycleView.destroy()
    lifecycleView.field.shouldBeNull()
  }

  @Test
  public fun onCreateView() {
    lifecycleView.show()
    lifecycleView.field shouldBe frameLayout
  }

  @Test
  public fun onDestroyView() {
    lifecycleView.hide()
    lifecycleView.field.shouldBeNull()
  }
}
