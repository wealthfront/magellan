package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Before
import org.junit.Test

internal class AttachLateinitFieldToLifecycleDelegateTest {

  private lateinit var parent: DummyLifecycleAwareComponent
  private lateinit var child: DummyLifecycleAwareComponent
  private lateinit var otherChild: DummyLifecycleAwareComponent

  @Before
  fun setUp() {
    parent = DummyLifecycleAwareComponent()
    child = DummyLifecycleAwareComponent()
    otherChild = DummyLifecycleAwareComponent()
  }

  @Test(expected = IllegalStateException::class)
  fun throwsBeforeSet() {
    val attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAwareComponent>()
    attachedChild.let { }
  }

  @Test
  fun attachesToLifecycle() {
    var attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAwareComponent>()

    parent.transitionToState(Created)
    child.currentState shouldBe Destroyed

    attachedChild = child
    attachedChild shouldBeSameInstanceAs child
    child.currentState shouldBe Created

    parent.transitionToState(Resumed)
    child.currentState shouldBe Resumed

    parent.transitionToState(Created)
    child.currentState shouldBe Created
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAwareComponent>()

    parent.transitionToState(Created)
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Destroyed

    attachedChild = child
    attachedChild shouldBeSameInstanceAs child
    child.currentState shouldBe Created
    otherChild.currentState shouldBe Destroyed

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Created

    parent.transitionToState(Resumed)
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Resumed

    parent.transitionToState(Created)
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Created
  }
}
