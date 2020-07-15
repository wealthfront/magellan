package com.wealthfront.magellan.navigation

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.NavigationType.GO
import com.wealthfront.magellan.NavigationType.SHOW
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Flow
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LinearNavigatorTest {

  @Mock lateinit var screenContainer: ScreenContainer

  private lateinit var screen1: Screen<*>
  private lateinit var screen2: Screen<*>
  private lateinit var flow1: Flow<*>
  private lateinit var screen3: Screen<*>
  private lateinit var flow2: Flow<*>
  private lateinit var screen4: Screen<*>
  private lateinit var linearNavigator : LinearNavigator

  @Before
  fun setUp() {
    initMocks(this)
    linearNavigator = LinearNavigator { screenContainer }

    screen1 = DummyScreen()
    screen2 = DummyScreen()
    flow1 = DummyFlow()
    screen3 = DummyScreen()
    flow2 = DummyFlow()
    screen4 = DummyScreen()
  }

  @Test
  fun goTo() {
    linearNavigator.goTo(screen1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun show() {
    linearNavigator.show(screen1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun replaceGo() {
    linearNavigator.show(screen1)
    linearNavigator.replaceAndGo(screen2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen2)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun replaceShow() {
    linearNavigator.show(screen1)
    linearNavigator.replaceAndShow(screen2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen2)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    linearNavigator.goTo(screen1)
    linearNavigator.goTo(screen2)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    linearNavigator.goTo(screen1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun goBack_flow_back() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(screen1, GO))
      it.push(NavigationEvent(screen2, GO))
      it.push(NavigationEvent(flow1, SHOW))
      it.push(NavigationEvent(screen3, GO))
      it.push(NavigationEvent(flow2, GO))
      it.push(NavigationEvent(screen4, GO))
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(5)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen1)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(GO)
  }

  @Test
  fun historyRewriter() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(screen1, GO))
      it.push(NavigationEvent(screen2, GO))
      it.push(NavigationEvent(flow1, SHOW))
      it.push(NavigationEvent(screen3, GO))
    }

    assertThat(linearNavigator.backStack.size).isEqualTo(4)
    assertThat(linearNavigator.backStack.first.navigable).isEqualTo(screen3)
    assertThat(linearNavigator.backStack.first.navigationType).isEqualTo(SHOW)
  }

  @Test
  fun goBack_withoutScreen_backOutOfApp() {
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
  }
}

class DummyScreen: Screen<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
class DummyFlow: Flow<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate, MagellanDummyLayoutBinding::container)
