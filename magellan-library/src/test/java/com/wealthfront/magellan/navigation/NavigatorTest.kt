package com.wealthfront.magellan.navigation

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.BACKWARD
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.R
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.core.getScreenTraversal
import com.wealthfront.magellan.core.getTraversalDescription
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorTest {

  @Mock lateinit var screenContainer: ScreenContainer

  private lateinit var navigable1: Screen
  private lateinit var navigable2: Screen
  private lateinit var navigable3: Screen
  private lateinit var navigator : Navigator

  @Before
  fun setUp() {
    initMocks(this)
    navigator = Navigator { screenContainer }

    navigable1 = DummyScreen1()
    navigable2 = DummyScreen2()
    navigable3 = DummyScreen3()
  }

  @Test
  fun goTo() {
    navigator.goTo(navigable1)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun show() {
    navigator.show(navigable1)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun replaceGo() {
    navigator.show(navigable1)
    navigator.replaceAndGo(navigable2)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable2)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun replaceShow() {
    navigator.show(navigable1)
    navigator.replaceAndShow(navigable2)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable2)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    navigator.goTo(navigable1)
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun historyRewriter() {
    navigator.navigate(FORWARD) {
      it.push(NavigationEvent(navigable1, FORWARD, GO))
      it.push(NavigationEvent(navigable2, FORWARD, GO))
      it.push(NavigationEvent(navigable3, FORWARD, SHOW))
    }

    assertThat(navigator.backStack.size).isEqualTo(3)
    assertThat(navigator.backStack.first.navigable).isEqualTo(navigable3)
    assertThat(navigator.backStack.first.direction).isEqualTo(FORWARD)
    assertThat(navigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_withoutScreen_backOutOfApp() {
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isFalse()
  }

  @Test
  fun getScreenTraversal() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)

    assertThat(navigable1.getScreenTraversal()).isEqualTo(listOf(navigable1, navigable2, navigable3))
  }

  @Test
  fun getTraversalDescription() {
    navigator.goTo(navigable1)
    navigator.goTo(navigable2)
    navigator.goTo(navigable3)

    assertThat(navigable1.getTraversalDescription())
      .isEqualTo("Backstack : DummyScreen1 -> DummyScreen2 -> DummyScreen3")
  }
}

class DummyScreen1: Screen(R.layout.magellan_dummy_layout)
class DummyScreen2: Screen(R.layout.magellan_dummy_layout)
class DummyScreen3: Screen(R.layout.magellan_dummy_layout)