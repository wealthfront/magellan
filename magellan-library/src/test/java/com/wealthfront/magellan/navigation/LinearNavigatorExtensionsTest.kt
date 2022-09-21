package com.wealthfront.magellan.navigation

import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.internal.test.DummyStep
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LinearNavigatorExtensionsTest {

  val navigable1 = DummyStep()
  val navigable2 = DummyStep()
  val navigable3 = DummyStep()

  lateinit var screenContainer: ScreenContainer
  lateinit var navigator: LinearNavigator

  @Before
  fun setUp() {
    val context = Robolectric.buildActivity(AppCompatActivity::class.java).get()
    screenContainer = ScreenContainer(context)
    navigator = DefaultLinearNavigator({ screenContainer })
  }

  @Test
  fun goBackTo_navigableInBackStack() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)
    navigator.goBackTo(navigable2)
    assertThat(navigator.backStack.first().navigable).isEqualTo(navigable2)
  }

  @Test
  fun goBackTo_navigableNotFound() {
    var throwable: Throwable? = null
    try {
      navigator.goTo(navigable1)
      navigator.goTo(navigable3)
      navigator.goBackTo(navigable2)
    } catch (t: Throwable) {
      throwable = t
    }

    assertThat(throwable).isNotNull()
    assertThat(throwable).isInstanceOf(IllegalStateException::class.java)
    assertThat((throwable as IllegalStateException).message)
      .isEqualTo("Navigable DummyStep not found in backStack")
  }

  @Test
  fun goBackTo_empty() {
    var throwable: Throwable? = null
    try {
      navigator.goBackTo(navigable2)
    } catch (t: Throwable) {
      throwable = t
    }

    assertThat(throwable).isNotNull()
    assertThat(throwable).isInstanceOf(IllegalStateException::class.java)
    assertThat((throwable as IllegalStateException).message)
      .isEqualTo("Navigable DummyStep not found in backStack")
  }

  @Test
  fun goBackBefore_navigableInBackStack() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)
    navigator.goBackBefore(navigable2)
    assertThat(navigator.backStack.first().navigable).isEqualTo(navigable1)
  }

  @Test
  fun goBackBefore_nothingBeforeNavigable() {
    var throwable: Throwable? = null
    try {
      navigator.goTo(navigable1)
      navigator.goTo(navigable2)
      navigator.goTo(navigable3)
      navigator.goBackBefore(navigable1)
    } catch (t: Throwable) {
      throwable = t
    }
    assertThat(navigator.backStack).isEmpty()
    assertThat((throwable as IllegalStateException).message)
      .isEqualTo("No Navigable before DummyStep")
  }

  @Test
  fun goBackBefore_navigableNotFound() {
    var throwable: Throwable? = null
    try {
      navigator.goTo(navigable1)
      navigator.goTo(navigable3)
      navigator.goBackBefore(navigable2)
    } catch (t: Throwable) {
      throwable = t
    }
    assertThat(navigator.backStack).hasSize(2)
    assertThat(throwable).isNotNull()
    assertThat((throwable as IllegalStateException).message)
      .isEqualTo("Navigable DummyStep not found in backStack")
  }

  @Test
  fun goBackBefore_empty() {
    var throwable: Throwable? = null
    try {
      navigator.goBackBefore(navigable2)
    } catch (t: Throwable) {
      throwable = t
    }
    assertThat(navigator.backStack).isEmpty()
    assertThat(throwable).isNotNull()
    assertThat((throwable as IllegalStateException).message)
      .isEqualTo("Navigable DummyStep not found in backStack")
  }

  @Test
  fun resetWithRoot() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable3)
    navigator.resetWithRoot(navigable2)
    assertThat(navigator.backStack).hasSize(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(navigable2)
  }

  @Test
  fun goBackToRoot() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)
    navigator.goBackToRoot()
    assertThat(navigator.backStack).hasSize(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(navigable1)
  }

  @Test
  fun goForwardToRoot() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)
    navigator.goForwardToRoot()
    assertThat(navigator.backStack).hasSize(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(navigable1)
  }

  @Test
  fun clearUntilRootAndGoTo() {
    val navigable4 = DummyStep()
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)
    navigator.clearUntilRootAndGoTo(navigable4)
    assertThat(navigator.backStack).hasSize(2)
    assertThat(navigator.backStack[0].navigable).isEqualTo(navigable4)
    assertThat(navigator.backStack[1].navigable).isEqualTo(navigable1)
  }
}
