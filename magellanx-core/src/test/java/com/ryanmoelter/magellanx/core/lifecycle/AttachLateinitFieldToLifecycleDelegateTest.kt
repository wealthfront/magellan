package com.ryanmoelter.magellanx.core.lifecycle

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Destroyed>()

    attachedChild = child
    attachedChild shouldBeSameInstanceAs child
    child.currentState should beInstanceOf<Created>()

    parent.transitionToState(Resumed(getApplicationContext()))
    child.currentState should beInstanceOf<Resumed>()

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Created>()
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAwareComponent>()

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Destroyed>()

    attachedChild = child
    attachedChild shouldBeSameInstanceAs child
    child.currentState should beInstanceOf<Created>()
    otherChild.currentState should beInstanceOf<Destroyed>()

    attachedChild = otherChild
    attachedChild shouldBeSameInstanceAs otherChild
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Created>()

    parent.transitionToState(Resumed(getApplicationContext()))
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Resumed>()

    parent.transitionToState(Created(getApplicationContext()))
    child.currentState should beInstanceOf<Destroyed>()
    otherChild.currentState should beInstanceOf<Created>()
  }
}
