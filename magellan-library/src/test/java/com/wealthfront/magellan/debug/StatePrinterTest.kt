package com.wealthfront.magellan.debug

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
public class StatePrinterTest {

  private lateinit var root: LifecycleAwareComponent
  private lateinit var context: Context

  @Before
  public fun setUp() {
    initMocks(this)
    context = getApplicationContext()
    root = DummyLifecycleOwner()
  }

  @Test
  public fun singleItem() {
    assertThat(root.getLifecycleStateSnapshot()).isEqualTo("DummyLifecycleOwner (Destroyed)\n")
  }

  @Test
  public fun singleChild() {
    root.attachToLifecycle(MyStep())
    assertThat(root.getLifecycleStateSnapshot()).isEqualTo(
      """
        DummyLifecycleOwner (Destroyed)
        └ MyStep (Destroyed)
      """.trimIndent() + '\n'
    )
  }

  @Test
  public fun multipleChildren() {
    root.attachToLifecycle(MyStep())
    root.attachToLifecycle(MyStep())
    root.create(context)
    root.show(context)
    assertThat(root.getLifecycleStateSnapshot()).isEqualTo(
      """
        DummyLifecycleOwner (Shown)
        ├ MyStep (Shown)
        └ MyStep (Shown)
      """.trimIndent() + '\n'
    )
  }

  @Test
  public fun complexTree() {
    root.attachToLifecycle(MyStep())
    val step = MyStep()
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(step) })
    step.attachToLifecycle(MySection())
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(MyLifecycleAwareThing()) })
    root.create(context)
    assertThat(root.getLifecycleStateSnapshot()).isEqualTo(
      """
        DummyLifecycleOwner (Created)
        ├ MyStep (Created)
        ├ MyJourney (Created)
        | └ MyStep (Created)
        |   └ MySection (Created)
        └ MyJourney (Created)
          └ MyLifecycleAwareThing (Created?)
      """.trimIndent() + '\n'
    )
  }
}

private class DummyLifecycleOwner : LifecycleAwareComponent()
private class MyJourney : LifecycleAwareComponent()
private class MyStep : LifecycleAwareComponent()
private class MySection : LifecycleAwareComponent()
private class MyLifecycleAwareThing : LifecycleAware
