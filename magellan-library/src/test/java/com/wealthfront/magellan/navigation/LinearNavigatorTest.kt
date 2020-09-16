package com.wealthfront.magellan.navigation

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LinearNavigatorTest {

  @Mock lateinit var screenContainer: ScreenContainer

  private lateinit var step1: Step<*>
  private lateinit var step2: Step<*>
  private lateinit var journey1: Journey<*>
  private lateinit var step3: Step<*>
  private lateinit var journey2: Journey<*>
  private lateinit var step4: Step<*>
  private lateinit var linearNavigator: LinearNavigator

  @Before
  fun setUp() {
    initMocks(this)
    linearNavigator = LinearNavigator { screenContainer }

    step1 = DummyStep()
    step2 = DummyStep()
    journey1 = DummyJourney()
    step3 = DummyStep()
    journey2 = DummyJourney()
    step4 = DummyStep()
  }

  @Test
  fun goTo() {
    linearNavigator.goTo(step1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun show() {
    linearNavigator.show(step1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun replaceGo() {
    linearNavigator.show(step1)
    linearNavigator.replaceAndGo(step2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun replaceShow() {
    linearNavigator.show(step1)
    linearNavigator.replaceAndShow(step2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    linearNavigator.goTo(step1)
    linearNavigator.goTo(step2)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    linearNavigator.goTo(step1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_journey_back() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(2)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().transition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun destroy() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
    }

    linearNavigator.resume(getApplicationContext())
    linearNavigator.goBack()
    linearNavigator.goBack()
    linearNavigator.pause(getApplicationContext())
    linearNavigator.destroy(getApplicationContext())

    assertThat(linearNavigator.backStack.size).isEqualTo(0)
  }

  @Test
  fun goBack_backOutOfJourney() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(journey1, ShowTransition()))
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
  }

  @Test
  fun goBack_withoutScreen_backOutOfApp() {
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
  }
}

private class DummyStep : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
private class DummyJourney : Journey<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate, MagellanDummyLayoutBinding::container)
