package com.ryanmoelter.magellanx.core.debug

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations.openMocks

public class StatePrinterTest {

  private lateinit var root: LifecycleAwareComponent
  private lateinit var mockSession: AutoCloseable

  @Before
  public fun setUp() {
    mockSession = openMocks(this)
    root = DummyLifecycleOwner()
  }

  @After
  public fun tearDown() {
    mockSession.close()
  }

  @Test
  public fun singleItem() {
    root.getLifecycleStateSnapshot() shouldBe "DummyLifecycleOwner (Destroyed)\n"
  }

  @Test
  public fun singleChild() {
    root.attachToLifecycle(MyStep())

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Destroyed)
      └ MyStep (Destroyed)
    """.trimIndent() + '\n'
  }

  @Test
  public fun multipleChildren() {
    root.attachToLifecycle(MyStep())
    root.attachToLifecycle(MyStep())
    root.create()
    root.show()

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Shown)
      ├ MyStep (Shown)
      └ MyStep (Shown)
    """.trimIndent() + '\n'
  }

  @Test
  public fun complexTree() {
    root.attachToLifecycle(MyStep())
    val step = MyStep()
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(step) })
    step.attachToLifecycle(MySection())
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(MyLifecycleAwareThing()) })
    root.create()

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Created)
      ├ MyStep (Created)
      ├ MyJourney (Created)
      | └ MyStep (Created)
      |   └ MySection (Created)
      └ MyJourney (Created)
        └ MyLifecycleAwareThing (Created?)
    """.trimIndent() + '\n'
  }
}

private class DummyLifecycleOwner : LifecycleAwareComponent()
private class MyJourney : LifecycleAwareComponent()
private class MyStep : LifecycleAwareComponent()
private class MySection : LifecycleAwareComponent()
private class MyLifecycleAwareThing : LifecycleAware
