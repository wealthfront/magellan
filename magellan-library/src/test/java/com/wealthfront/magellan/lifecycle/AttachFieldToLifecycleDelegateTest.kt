package com.wealthfront.magellan.lifecycle

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
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

    assertThat(attachedChild).isSameInstanceAs(child)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Resumed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Created::class.java)
  }

  @Test
  fun exposesCustomProperty() {
    val attachedCurrentState by parent.attachFieldToLifecycle(child) { it.currentState }

    assertThat(attachedCurrentState).isSameInstanceAs(child.currentState)
    assertThat(attachedCurrentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(attachedCurrentState).isInstanceOf(Resumed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(attachedCurrentState).isInstanceOf(Created::class.java)
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    attachedChild = otherChild
    assertThat(attachedChild).isSameInstanceAs(otherChild)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(otherChild.currentState).isInstanceOf(Resumed::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(otherChild.currentState).isInstanceOf(Created::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
  }

  @Test
  fun overrideValue_afterLifecycleEvents() {
    var attachedChild by parent.attachFieldToLifecycle(child)

    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(child.currentState).isInstanceOf(Resumed::class.java)

    attachedChild = otherChild
    assertThat(attachedChild).isSameInstanceAs(otherChild)
    assertThat(otherChild.currentState).isInstanceOf(Resumed::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(otherChild.currentState).isInstanceOf(Created::class.java)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
  }

  @Test
  fun overrideValue_customValue() {
    var overriddenProperty by parent.attachFieldToLifecycle(child) { it.dummyProperty }

    assertThat(overriddenProperty).isEqualTo(0)
    overriddenProperty = 20
    assertThat(overriddenProperty).isEqualTo(20)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)

    child.dummyProperty = 30
    assertThat(overriddenProperty).isEqualTo(20)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(overriddenProperty).isEqualTo(20)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(overriddenProperty).isEqualTo(20)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)
  }
}

private class DummyLifecycleAwareComponent(var dummyProperty: Int = 0) : LifecycleAwareComponent()
