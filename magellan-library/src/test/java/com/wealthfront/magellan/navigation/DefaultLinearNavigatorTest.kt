package com.wealthfront.magellan.navigation

import android.app.Activity
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.internal.test.DummyStep
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.fakes.RoboMenu

@RunWith(RobolectricTestRunner::class)
internal class DefaultLinearNavigatorTest {

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
  private lateinit var navigator: DefaultLinearNavigator
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

    navigator = DefaultLinearNavigator { screenContainer }
    navigator.create(context)
  }

  @Test
  fun goTo() {
    navigator.goTo(step1)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test(expected = IllegalStateException::class)
  fun goTo_existingStep() {
    navigator.goTo(step1)
    navigator.goTo(step1)
  }

  @Test
  fun show() {
    navigator.goTo(step1, ShowTransition())

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun replaceGo() {
    navigator.goTo(step1, ShowTransition())
    navigator.replace(step2)

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun replaceShow() {
    navigator.goTo(step1, ShowTransition())
    navigator.replace(step2, ShowTransition())

    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(ShowTransition::class.java)
  }

  @Test(expected = IllegalStateException::class)
  fun navigate_duplicateStep() {
    navigator.navigate(FORWARD) { deque ->
      deque.push(NavigationEvent(step1, DefaultTransition()))
      deque.push(NavigationEvent(step1, DefaultTransition()))
      deque.peek()!!
    }
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    navigator.goTo(step1)
    navigator.goTo(step2)
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    navigator.goTo(step1)
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(navigator.backStack.size).isEqualTo(1)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_journey_back() {
    activityController.restart()
    navigator.show(context)
    navigator.resume(context)
    navigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!
    }

    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(navigator.backStack.size).isEqualTo(2)
    assertThat(navigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(navigator.backStack.first().magellanTransition.javaClass).isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun destroy() {
    activityController.restart()
    navigator.show(context)
    navigator.resume(context)
    navigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!
    }

    assertThat(navigator.backStack.size).isEqualTo(3)

    navigator.pause(context)
    navigator.hide(context)
    navigator.destroy(context)

    assertThat(navigator.backStack.size).isEqualTo(0)
  }

  @Test
  fun goBack_backOutOfJourney() {
    navigator.navigate(FORWARD) {
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!
    }

    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(navigator.backStack.size).isEqualTo(1)
  }

  @Test
  fun goBack_withoutScreen_backOutOfApp() {
    val didNavigate = navigator.goBack()

    assertThat(didNavigate).isFalse()
  }
}

private open class FakeActivity : AppCompatActivity()

private open class DummyJourney :
  Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  )
