package com.ryanmoelter.magellanx.core.lifecycle

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Resumed(getApplicationContext()))
    child.currentState should beInstanceOf<Resumed>()

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Created>()
  }

  @Test
  fun exposesCustomProperty() {
    val attachedCurrentState by parent.attachFieldToLifecycle(child) { it.currentState }

    attachedCurrentState shouldBeSameInstanceAs child.currentState
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Resumed(getApplicationContext()))
    child.currentState should beInstanceOf<Resumed>()

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Created>()
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    otherChild.currentState should beInstanceOf<Destroyed>()
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Resumed(getApplicationContext()))
    otherChild.currentState should beInstanceOf<Resumed>()
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Created(getApplicationContext()))
    otherChild.currentState should beInstanceOf<Created>()
    child.currentState should beInstanceOf<Destroyed>()
  }

  @Test
  fun overrideValue_afterLifecycleEvents() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    otherChild.currentState should beInstanceOf<Destroyed>()
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Resumed(getApplicationContext()))
    otherChild.currentState should beInstanceOf<Destroyed>()
    child.currentState should beInstanceOf<Resumed>()

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    otherChild.currentState should beInstanceOf<Resumed>()
    child.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Created(getApplicationContext()))
    otherChild.currentState should beInstanceOf<Created>()
    child.currentState should beInstanceOf<Destroyed>()
  }

  @Test
  fun overrideValue_customValue() {
    var overriddenProperty by parent.attachFieldToLifecycle(child) { it.dummyProperty }

    overriddenProperty shouldBe 0
    overriddenProperty = 20
    overriddenProperty shouldBe 20
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Destroyed>()

    child.dummyProperty = 30
    overriddenProperty shouldBe 20

    parent.transitionToState(Resumed(getApplicationContext()))
    overriddenProperty shouldBe 20
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Destroyed>()

    parent.transitionToState(Created(getApplicationContext()))
    overriddenProperty shouldBe 20
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Destroyed>()
  }
}
