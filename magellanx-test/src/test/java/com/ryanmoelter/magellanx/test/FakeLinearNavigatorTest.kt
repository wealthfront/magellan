package com.ryanmoelter.magellanx.test

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigationEvent
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.compose.transitions.defaultTransition
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

@OptIn(ExperimentalAnimationApi::class)
public class FakeLinearNavigatorTest {

  private lateinit var navigator: FakeComposeNavigator
  @Mock internal lateinit var context: Context

  @Before
  public fun setUp() {
    initMocks(this)
    navigator = FakeComposeNavigator()
  }

  @Test
  public fun clear() {
    navigator.backStack = listOf(ComposeNavigationEvent(TestNavigable(), defaultTransition))
    navigator.clear()
    navigator.backStack.shouldBeEmpty()
    navigator.currentNavigable.shouldBeNull()
  }

  @Test
  public fun goTo() {
    navigator.goTo(TestNavigable())
    navigator.backStack shouldHaveSize 1

    val topNavigable = TestNavigable()
    navigator.goTo(topNavigable)
    navigator.backStack shouldHaveSize 2
    navigator.currentNavigable shouldBeSameInstanceAs topNavigable
  }

  @Test
  public fun replace() {
    navigator.goTo(TestNavigable())
    navigator.backStack shouldHaveSize 1

    val topNavigable = TestNavigable()
    navigator.replace(topNavigable)
    navigator.backStack shouldHaveSize 1
    navigator.currentNavigable shouldBeSameInstanceAs topNavigable
  }

  @Test
  public fun navigate() {
    val originalBottomNavigable = TestNavigable()
    val newBottomNavigable = TestNavigable()
    val newTopNavigable = TestNavigable()
    navigator.backStack = listOf(
      ComposeNavigationEvent(TestNavigable(), defaultTransition),
      ComposeNavigationEvent(originalBottomNavigable, defaultTransition),
    )

    navigator.navigate(Direction.FORWARD) {
      listOf(
        ComposeNavigationEvent(newBottomNavigable, defaultTransition),
        ComposeNavigationEvent(originalBottomNavigable, defaultTransition),
        ComposeNavigationEvent(newTopNavigable, defaultTransition)
      )
    }
    navigator.backStack.map { it.navigable } shouldContainExactly listOf(
      newTopNavigable,
      originalBottomNavigable,
      newBottomNavigable,
    )
    navigator.currentNavigable shouldBeSameInstanceAs newTopNavigable
  }

  @Test
  public fun goBack_noop() {
    navigator.goBack().shouldBeFalse()
    navigator.goTo(TestNavigable())
    navigator.goBack().shouldBeFalse()
    navigator.backStack shouldHaveSize 1
  }

  @Test
  public fun goBack_success() {
    val bottomNavigable = TestNavigable()
    navigator.backStack = listOf(
      ComposeNavigationEvent(TestNavigable(), defaultTransition),
      ComposeNavigationEvent(bottomNavigable, defaultTransition)
    )
    navigator.goBack().shouldBeTrue()
    navigator.backStack shouldHaveSize 1
    navigator.currentNavigable shouldBeSameInstanceAs bottomNavigable
  }

  @Test
  public fun destroy() {
    navigator.backStack = listOf(ComposeNavigationEvent(TestNavigable(), defaultTransition))
    navigator.destroy(context)
    navigator.backStack.shouldBeEmpty()
    navigator.currentNavigable.shouldBeNull()
  }
}
