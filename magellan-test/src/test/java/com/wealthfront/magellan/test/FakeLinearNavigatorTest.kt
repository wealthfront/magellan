package com.wealthfront.magellan.test

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.init.getDefaultTransition
import com.wealthfront.magellan.navigation.NavigationEvent
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

public class FakeLinearNavigatorTest {

  private lateinit var navigator: FakeLinearNavigator
  @Mock internal lateinit var context: Context

  @Before
  public fun setUp() {
    initMocks(this)
    navigator = FakeLinearNavigator()
  }

  @Test
  public fun clear() {
    navigator.backStack = listOf(NavigationEvent(TestNavigable(), getDefaultTransition()))
    navigator.clear()
    assertThat(navigator.backStack).isEmpty()
    assertThat(navigator.currentNavigable).isNull()
  }

  @Test
  public fun goTo() {
    navigator.goTo(TestNavigable())
    assertThat(navigator.backStack).hasSize(1)

    val topNavigable = TestNavigable()
    navigator.goTo(topNavigable)
    assertThat(navigator.backStack).hasSize(2)
    assertThat(navigator.currentNavigable).isSameInstanceAs(topNavigable)
  }

  @Test
  public fun replace() {
    navigator.goTo(TestNavigable())
    assertThat(navigator.backStack).hasSize(1)

    val topNavigable = TestNavigable()
    navigator.replace(topNavigable)
    assertThat(navigator.backStack).hasSize(1)
    assertThat(navigator.currentNavigable).isSameInstanceAs(topNavigable)
  }

  @Test
  public fun navigate() {
    val originalBottomNavigable = TestNavigable()
    val newBottomNavigable = TestNavigable()
    val newTopNavigable = TestNavigable()
    navigator.backStack = listOf(
      NavigationEvent(TestNavigable(), getDefaultTransition()),
      NavigationEvent(originalBottomNavigable, getDefaultTransition()),
    )

    navigator.navigate(Direction.FORWARD) {
      it.clear()
      it.push(NavigationEvent(newBottomNavigable, getDefaultTransition()))
      it.push(NavigationEvent(originalBottomNavigable, getDefaultTransition()))
      it.push(NavigationEvent(newTopNavigable, getDefaultTransition()))
      getDefaultTransition()
    }
    assertThat(navigator.backStack.map { it.navigable }).containsExactly(
      newTopNavigable,
      originalBottomNavigable,
      newBottomNavigable,
    ).inOrder()
    assertThat(navigator.currentNavigable).isSameInstanceAs(newTopNavigable)
  }

  @Test
  public fun goBack_noop() {
    assertThat(navigator.goBack()).isFalse()
    navigator.goTo(TestNavigable())
    assertThat(navigator.goBack()).isFalse()
    assertThat(navigator.backStack).hasSize(1)
  }

  @Test
  public fun goBack_success() {
    val bottomNavigable = TestNavigable()
    navigator.backStack = listOf(
      NavigationEvent(TestNavigable(), getDefaultTransition()),
      NavigationEvent(bottomNavigable, getDefaultTransition())
    )
    assertThat(navigator.goBack()).isTrue()
    assertThat(navigator.backStack).hasSize(1)
    assertThat(navigator.currentNavigable).isSameInstanceAs(bottomNavigable)
  }

  @Test
  public fun destroy() {
    navigator.backStack = listOf(NavigationEvent(TestNavigable(), getDefaultTransition()))
    navigator.destroy(context)
    assertThat(navigator.backStack).isEmpty()
    assertThat(navigator.currentNavigable).isNull()
  }
}
