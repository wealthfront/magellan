package com.wealthfront.magellan.navigation

import android.app.Activity
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import com.wealthfront.magellan.view.ActionBarConfig
import com.wealthfront.magellan.view.ActionBarModifier
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.fakes.RoboMenu

@RunWith(RobolectricTestRunner::class)
class LinearNavigatorTest {

  private val fakeId = 1000

  @Mock lateinit var screenContainer: ScreenContainer

  private lateinit var activityController: ActivityController<FakeActivity>
  private lateinit var menu: Menu
  private lateinit var step1: Step<*>
  private lateinit var step2: Step<*>
  private lateinit var journey1: Journey<*>
  private lateinit var step3: Step<*>
  private lateinit var journey2: Journey<*>
  private lateinit var step4: Step<*>
  private lateinit var linearNavigator: LinearNavigator
  private lateinit var context: Activity

  @Before
  fun setUp() {
    initMocks(this)
    step1 = spy(DummyStep())
    journey1 = spy(DummyJourney())
    step2 = DummyStep()
    step3 = DummyStep()
    journey2 = DummyJourney()
    step4 = DummyStep()
    activityController = buildActivity(FakeActivity::class.java)
    context = spy(activityController.get())
    menu = RoboMenu(context)
    menu.add(0, fakeId, 0, "some string")

    linearNavigator = LinearNavigator(journey1) { screenContainer }
    linearNavigator.create(context)
  }

  @Test
  fun goTo() {
    linearNavigator.menu = menu

    verify(journey1 as ActionBarModifier).onUpdateMenu(menu)

    linearNavigator.goTo(step1)

    verify(step1).setTitle(anyString())
    verify(journey1 as ActionBarModifier, times(2)).onUpdateMenu(menu)
    verify(step1 as ActionBarModifier).onUpdateMenu(menu)
    verify(context as ActionBarConfigListener).onNavigate(any())
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun show() {
    linearNavigator.goTo(step1, ShowTransition())

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun replaceGo() {
    linearNavigator.goTo(step1, ShowTransition())
    linearNavigator.replace(step2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun replaceShow() {
    linearNavigator.goTo(step1, ShowTransition())
    linearNavigator.replace(step2, ShowTransition())

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    linearNavigator.goTo(step1)
    linearNavigator.goTo(step2)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    linearNavigator.goTo(step1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_journey_back() {
    activityController.restart()
    linearNavigator.resume(context)
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(2)
    assertThat(linearNavigator.backStack.peek().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.peek().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun destroy() {
    activityController.restart()
    linearNavigator.resume(context)
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
    }

    linearNavigator.goBack()
    linearNavigator.goBack()
    linearNavigator.pause(context)
    linearNavigator.destroy(context)

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

private open class FakeActivity : AppCompatActivity(), ActionBarConfigListener {
  override fun onNavigate(actionBarConfig: ActionBarConfig?) {}
}

private open class DummyStep : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate), ActionBarModifier
private open class DummyJourney :
  Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ),
  ActionBarModifier
