package com.wealthfront.magellan.navigation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.init.NavigationOverride
import com.wealthfront.magellan.internal.test.DummyStep
import com.wealthfront.magellan.internal.test.TransitionState.FINISHED
import com.wealthfront.magellan.internal.test.TransitionState.NOT_STARTED
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
internal class DefaultLinearNavigatorTest {

  private lateinit var activityController: ActivityController<FakeActivity>
  private lateinit var screenContainer: ScreenContainer
  private lateinit var step1: DummyStep
  private lateinit var step2: DummyStep
  private lateinit var journey1: DummyJourney
  private lateinit var step3: DummyStep
  private lateinit var journey2: DummyJourney
  private lateinit var step4: DummyStep
  private lateinit var linearNavigator: DefaultLinearNavigator
  private lateinit var context: Activity

  @Before
  fun setUp() {
    initMocks(this)
    step1 = DummyStep()
    journey1 = DummyJourney()
    step2 = DummyStep()
    step3 = DummyStep()
    journey2 = DummyJourney()
    step4 = DummyStep()
    activityController = buildActivity(FakeActivity::class.java)
    context = activityController.get()
    screenContainer = ScreenContainer(context)

    linearNavigator = DefaultLinearNavigator({ screenContainer }, emptySet())
    linearNavigator.create(context)
  }

  @Test
  fun goTo() {
    linearNavigator.goTo(step1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test(expected = IllegalStateException::class)
  fun goTo_existingStep() {
    try {
      linearNavigator.goTo(step1)
      linearNavigator.goTo(step1)
    } catch (exception: IllegalStateException) {
      assertThat(exception.message).isEqualTo(
        "Cannot have multiple of the same Navigable in the backstack. " +
          "Have 1 extra Navigables in: [DummyStep, DummyStep]"
      )
      throw exception
    }
  }

  @Test
  fun show() {
    linearNavigator.goTo(step1, ShowTransition())

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(ShowTransition::class.java)
  }

  @Test
  fun show_navigationRequestHandler() {
    linearNavigator = DefaultLinearNavigator(
      { screenContainer },
      setOf(
        NavigationOverride(
          { _, navigable -> navigable == step2 },
          { delegate -> delegate.goTo(step3) }
        )
      )
    )
    linearNavigator.create(context)

    linearNavigator.goTo(step1, ShowTransition())
    linearNavigator.goTo(step2, ShowTransition())

    assertThat(linearNavigator.backStack.map { it.navigable })
      .containsExactly(step3, step1)
  }

  @Test
  fun replaceGo() {
    linearNavigator.goTo(step1, ShowTransition())
    linearNavigator.replace(step2)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun replaceShow() {
    linearNavigator.goTo(step1, ShowTransition())
    linearNavigator.replace(step2, ShowTransition())

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(ShowTransition::class.java)
  }

  @Test(expected = IllegalStateException::class)
  fun navigate_duplicateStep() {
    linearNavigator.navigate(FORWARD) { deque ->
      deque.push(NavigationEvent(step1, DefaultTransition()))
      deque.push(NavigationEvent(step1, DefaultTransition()))
      deque.peek()!!.magellanTransition
    }
  }

  @Test
  fun goBack_multipleScreen_removeScreenFromBackstack() {
    linearNavigator.goTo(step1)
    linearNavigator.goTo(step2)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_oneScreen_backOutOfApp() {
    linearNavigator.goTo(step1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_journey_back() {
    activityController.restart()
    linearNavigator.show(context)
    linearNavigator.resume(context)
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(2)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step2)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun destroy() {
    activityController.restart()
    linearNavigator.show(context)
    linearNavigator.resume(context)
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
    }

    assertThat(linearNavigator.backStack.size).isEqualTo(3)

    linearNavigator.pause(context)
    linearNavigator.hide(context)
    linearNavigator.destroy(context)

    assertThat(linearNavigator.backStack.size).isEqualTo(0)
  }

  @Test
  fun goBack_backOutOfJourney() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
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

  @Test
  fun resumed_screenPopulated() {
    linearNavigator.goTo(step1)
    linearNavigator.transitionToState(Created(context))
    assertThat(screenContainer.childCount).isEqualTo(0)
    assertThat(step1.currentTransitionState).isEqualTo(NOT_STARTED)
    linearNavigator.transitionToState(Shown(context))
    assertThat(screenContainer.childCount).isEqualTo(1)
    assertThat(step1.currentState).isInstanceOf(Shown::class.java)
    assertThat(step1.currentTransitionState).isEqualTo(FINISHED)
  }
}

private open class FakeActivity : AppCompatActivity()

private open class DummyJourney :
  Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  )
