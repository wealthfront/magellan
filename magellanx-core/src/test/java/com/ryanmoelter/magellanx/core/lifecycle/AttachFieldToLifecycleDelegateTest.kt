package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Before
import org.junit.Test

internal class AttachFieldToLifecycleDelegateTest {

  private lateinit var parent: DummyLifecycleAwareComponent
  private lateinit var child: DummyLifecycleAwareComponent
  private lateinit var otherChild: DummyLifecycleAwareComponent

  @Before
  fun setUp() {
    parent = DummyLifecycleAwareComponent()
    child = DummyLifecycleAwareComponent()
    otherChild = DummyLifecycleAwareComponent()
  }

  @Test
  fun attachesToLifecycle() {
    val attachedChild by parent.attachFieldToLifecycle(child)

    attachedChild shouldBeSameInstanceAs child
    child.currentState shouldBe Destroyed

    parent.transitionToState(Resumed)
    child.currentState shouldBe Resumed

    parent.transitionToState(Created)
    child.currentState shouldBe Created
  }

  @Test
  fun exposesCustomProperty() {
    val attachedCurrentState by parent.attachFieldToLifecycle(child) { it.currentState }

    attachedCurrentState shouldBeSameInstanceAs child.currentState
    child.currentState shouldBe Destroyed

    parent.transitionToState(Resumed)
    child.currentState shouldBe Resumed

    parent.transitionToState(Created)
    child.currentState shouldBe Created
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    otherChild.currentState shouldBe Destroyed
    child.currentState shouldBe Destroyed

    parent.transitionToState(Resumed)
    otherChild.currentState shouldBe Resumed
    child.currentState shouldBe Destroyed

    parent.transitionToState(Created)
    otherChild.currentState shouldBe Created
    child.currentState shouldBe Destroyed
  }

  @Test
  fun overrideValue_afterLifecycleEvents() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    otherChild.currentState shouldBe Destroyed
    child.currentState shouldBe Destroyed

    parent.transitionToState(Resumed)
    otherChild.currentState shouldBe Destroyed
    child.currentState shouldBe Resumed

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    otherChild.currentState shouldBe Resumed
    child.currentState shouldBe Destroyed

    parent.transitionToState(Created)
    otherChild.currentState shouldBe Created
    child.currentState shouldBe Destroyed
  }

  @Test
  fun overrideValue_customValue() {
    var overriddenProperty by parent.attachFieldToLifecycle(child) { it.dummyProperty }

    overriddenProperty shouldBe 0
    overriddenProperty = 20
    overriddenProperty shouldBe 20
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Destroyed

    child.dummyProperty = 30
    overriddenProperty shouldBe 20

    parent.transitionToState(Resumed)
    overriddenProperty shouldBe 20
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Destroyed

    parent.transitionToState(Created)
    overriddenProperty shouldBe 20
    child.currentState shouldBe Destroyed
    otherChild.currentState shouldBe Destroyed
  }
}
