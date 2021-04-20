package com.wealthfront.magellan.debug

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.LifecycleOwner
import org.junit.Before
import org.junit.Test

public class StatePrinterTest {

  private lateinit var root: LifecycleOwner

  @Before
  public fun setUp() {
    root = DummyLifecycleOwner()
  }

  @Test
  public fun singleItem() {
    assertThat(root.getTreeDescription()).isEqualTo("DummyLifecycleOwner\n")
  }

  @Test
  public fun singleChild() {
    root.attachToLifecycle(MyStep())
    assertThat(root.getTreeDescription()).isEqualTo(
      """
        DummyLifecycleOwner
        └ MyStep
      """.trimIndent() + '\n'
    )
  }

  @Test
  public fun multipleChildren() {
    root.attachToLifecycle(MyStep())
    root.attachToLifecycle(MyStep())
    assertThat(root.getTreeDescription()).isEqualTo(
      """
        DummyLifecycleOwner
        ├ MyStep
        └ MyStep
      """.trimIndent() + '\n'
    )
  }

  @Test
  public fun complexTree() {
    root.attachToLifecycle(MyStep())
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(MyStep()) })
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(MyLifecycleAwareThing()) })
    assertThat(root.getTreeDescription()).isEqualTo(
      """
        DummyLifecycleOwner
        ├ MyStep
        ├ MyJourney
        | └ MyStep
        └ MyJourney
          └ MyLifecycleAwareThing
      """.trimIndent() + '\n'
    )
  }
}

private class DummyLifecycleOwner : LifecycleAwareComponent()
private class MyJourney : LifecycleAwareComponent()
private class MyStep : LifecycleAwareComponent()
private class MyLifecycleAwareThing : LifecycleAware
