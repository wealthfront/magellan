package com.wealthfront.magellan.lifecycle

import androidx.test.core.app.ApplicationProvider
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
internal class AttachLateinitFieldToLifecycleDelegateTest {

  private lateinit var parent: DummyLifecycleAware
  private lateinit var child: DummyLifecycleAware
  private lateinit var otherChild: DummyLifecycleAware

  @Before
  fun setUp() {
    parent = DummyLifecycleAware()
    child = DummyLifecycleAware()
    otherChild = DummyLifecycleAware()
  }

  @Test(expected = IllegalStateException::class)
  fun throwsBeforeSet() {
    val attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAware>()
    attachedChild.let { }
  }

  @Test
  fun attachesToLifecycle() {
    var attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAware>()

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)

    attachedChild = child
    assertThat(attachedChild).isSameInstanceAs(child)
    assertThat(child.currentState).isInstanceOf(Created::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Resumed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Created::class.java)
  }

  @Test
  fun overrideValue() {
    var attachedChild by parent.attachLateinitFieldToLifecycle<DummyLifecycleAware>()

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)

    attachedChild = child
    assertThat(attachedChild).isSameInstanceAs(child)
    assertThat(child.currentState).isInstanceOf(Created::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Destroyed::class.java)

    attachedChild = otherChild
    assertThat(attachedChild).isSameInstanceAs(otherChild)
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Created::class.java)

    parent.transitionToState(Resumed(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Resumed::class.java)

    parent.transitionToState(Created(getApplicationContext()))
    assertThat(child.currentState).isInstanceOf(Destroyed::class.java)
    assertThat(otherChild.currentState).isInstanceOf(Created::class.java)
  }
}

private class DummyLifecycleAware(var dummyProperty: Int = 0) : LifecycleAwareComponent()
