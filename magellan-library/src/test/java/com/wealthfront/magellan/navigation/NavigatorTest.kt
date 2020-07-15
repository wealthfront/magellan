package com.wealthfront.magellan.navigation

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.R
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Screen
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
  private lateinit var linearNavigator : LinearNavigator

  @Before
  fun setUp() {
    initMocks(this)
    linearNavigator = LinearNavigator { screenContainer }

    navigable1 = DummyScreen1()
    navigable2 = DummyScreen2()
    navigable3 = DummyScreen3()
  }

  @Test
  fun goTo() {
    linearNavigator.goTo(navigable1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun show() {
    linearNavigator.show(navigable1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun replaceGo() {
    linearNavigator.show(navigable1)
    linearNavigator.replaceAndGo(navigable2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable2)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun replaceShow() {
    linearNavigator.show(navigable1)
    linearNavigator.replaceAndShow(navigable2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable2)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    linearNavigator.goTo(navigable1)
    linearNavigator.goTo(navigable2)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    linearNavigator.goTo(navigable1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun historyRewriter() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(navigable1, GO))
      it.push(NavigationEvent(navigable2, GO))
      it.push(NavigationEvent(navigable3, SHOW))
    }

    assertThat(linearNavigator.backStack.size).isEqualTo(3)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(navigable3)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_withoutScreen_backOutOfApp() {
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
  }
}

class DummyScreen1: Screen(R.layout.magellan_dummy_layout)
class DummyScreen2: Screen(R.layout.magellan_dummy_layout)
class DummyScreen3: Screen(R.layout.magellan_dummy_layout)