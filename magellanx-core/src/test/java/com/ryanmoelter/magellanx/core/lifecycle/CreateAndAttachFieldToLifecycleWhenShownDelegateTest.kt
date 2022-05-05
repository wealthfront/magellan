package com.ryanmoelter.magellanx.core.lifecycle

import android.content.Context
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

  @Mock
  private lateinit var context: Context

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
    lifecycleView.create(context)
    lifecycleView.field.shouldBeNull()

    lifecycleView.show(context)
    lifecycleView.field shouldBe frameLayout

    lifecycleView.resume(context)
    lifecycleView.field shouldBe frameLayout

    lifecycleView.pause(context)
    lifecycleView.field shouldBe frameLayout

    lifecycleView.hide(context)
    lifecycleView.field.shouldBeNull()

    lifecycleView.destroy(context)
    lifecycleView.field.shouldBeNull()
  }

  @Test
  public fun onCreateView() {
    lifecycleView.show(context)
    lifecycleView.field shouldBe frameLayout
  }

  @Test
  public fun onDestroyView() {
    lifecycleView.hide(context)
    lifecycleView.field.shouldBeNull()
  }
}
