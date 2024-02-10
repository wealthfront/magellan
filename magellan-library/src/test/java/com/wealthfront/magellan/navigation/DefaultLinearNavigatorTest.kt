package com.wealthfront.magellan.navigation

import android.app.Activity
import android.os.Looper.getMainLooper
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.Direction.FORWARD
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.internal.test.DummyStep
import com.wealthfront.magellan.internal.test.TransitionState.FINISHED
import com.wealthfront.magellan.internal.test.TransitionState.NOT_STARTED
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Resumed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import com.wealthfront.magellan.transitions.ShowTransition
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class DefaultLinearNavigatorTest {

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
  @MockK private lateinit var navigableListener: NavigationListener

  @Before
  fun setUp() {
    init(this)
    initMocks()
    NavigationPropagator.addNavigableListener(navigableListener)

    step1 = DummyStep()
    journey1 = DummyJourney()
    step2 = DummyStep()
    step3 = DummyStep()
    journey2 = DummyJourney()
    step4 = DummyStep()
    activityController = buildActivity(FakeActivity::class.java)
    context = activityController.get()
    screenContainer = ScreenContainer(context)

    linearNavigator = DefaultLinearNavigator({ screenContainer })
    linearNavigator.transitionToState(Created(context))
  }

  @After
  fun tearDown() {
    NavigationPropagator.removeNavigableListener(navigableListener)
  }

  private fun initMocks() {
    every { navigableListener.onNavigatedTo(any()) }.answers { }
    every { navigableListener.onNavigatedFrom(any()) }.answers { }
    every { navigableListener.beforeNavigation() }.answers { }
    every { navigableListener.afterNavigation() }.answers { }
  }

  @Test
  fun goTo() {
    linearNavigator.transitionToState(Resumed(context))
    linearNavigator.goTo(step1)

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)

    verify { navigableListener.beforeNavigation() }
    verify(exactly = 0) { navigableListener.onNavigatedFrom(any()) }
    verify { navigableListener.onNavigatedTo(step1) }

    step1.view!!.viewTreeObserver.dispatchOnPreDraw()
    shadowOf(getMainLooper()).idle()
    verify { navigableListener.afterNavigation() }
  }

  @Test
  fun goTo_viewDoesNotExist() {
    linearNavigator.goTo(step1)

    verify { navigableListener.beforeNavigation() }
    verify(exactly = 0) { navigableListener.onNavigatedFrom(any()) }
    verify { navigableListener.onNavigatedTo(step1) }
    verify(exactly = 0) { navigableListener.afterNavigation() }
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
    linearNavigator.transitionToState(Resumed(context))
    linearNavigator.goTo(step1, ShowTransition())

    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(ShowTransition::class.java)

    verify { navigableListener.beforeNavigation() }
    verify(exactly = 0) { navigableListener.onNavigatedFrom(any()) }
    verify { navigableListener.onNavigatedTo(step1) }

    step1.view!!.viewTreeObserver.dispatchOnPreDraw()
    shadowOf(getMainLooper()).idle()
    verify { navigableListener.afterNavigation() }
  }

  @Test
  fun show_navigationRequestHandler() {
    linearNavigator = DefaultLinearNavigator(
      { screenContainer },
      object : NavigationOverrideProvider {
        override fun getNavigationOverrides(): List<NavigationOverride> {
          return listOf(
            NavigationOverride(
              { _, navigable -> navigable == step2 },
              { delegate, _ -> delegate.goTo(step3) }
            )
          )
        }
      }
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
    linearNavigator.transitionToState(Resumed(context))
    linearNavigator.goTo(step1, NoAnimationTransition())
    step1.view!!.measure(200, 200)
    step1.view!!.viewTreeObserver.dispatchOnPreDraw()
    linearNavigator.goTo(step2, NoAnimationTransition())
    step2.view!!.measure(200, 200)
    step2.view!!.viewTreeObserver.dispatchOnPreDraw()
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isTrue()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(NoAnimationTransition::class.java)

    verify { navigableListener.beforeNavigation() }
    verify { navigableListener.onNavigatedFrom(step2) }
    verify { navigableListener.onNavigatedTo(step1) }
    step1.view!!.viewTreeObserver.dispatchOnPreDraw()
    shadowOf(getMainLooper()).idle()
    verify { navigableListener.afterNavigation() }
  }

  @Test
  fun goBack_journey_back() {
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
  fun goBack_oneScreen() {
    linearNavigator.goTo(step1)
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(step1)
    assertThat(linearNavigator.backStack.first().magellanTransition.javaClass)
      .isEqualTo(DefaultTransition::class.java)
  }

  @Test
  fun goBack_oneJourney() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
    }

    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(journey1)
  }

  @Test
  fun onBackPressed_oneJourney() {
    linearNavigator.transitionToState(Resumed(context))
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
    }

    val didNavigate = linearNavigator.backPressed()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(1)
    assertThat(linearNavigator.backStack.first().navigable).isEqualTo(journey1)
    assertThat(journey1.onBackPressedCount).isEqualTo(1)
  }

  @Test
  fun goBack_withoutScreen() {
    val didNavigate = linearNavigator.goBack()

    assertThat(didNavigate).isFalse()
    assertThat(linearNavigator.backStack.size).isEqualTo(0)
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

  @Test
  fun destroy() {
    linearNavigator.navigate(FORWARD) {
      it.push(NavigationEvent(step1, DefaultTransition()))
      it.push(NavigationEvent(step2, DefaultTransition()))
      it.push(NavigationEvent(journey1, ShowTransition()))
      it.first()!!.magellanTransition
    }

    assertThat(linearNavigator.backStack.size).isEqualTo(3)

    linearNavigator.transitionToState(Destroyed)

    assertThat(linearNavigator.backStack.size).isEqualTo(0)
  }
}

private open class FakeActivity : AppCompatActivity()

private open class DummyJourney : Journey<MagellanDummyLayoutBinding>(
  MagellanDummyLayoutBinding::inflate,
  MagellanDummyLayoutBinding::container
) {

  var onBackPressedCount: Int = 0
    private set

  override fun onBackPressed(): Boolean {
    onBackPressedCount += 1
    return super.onBackPressed()
  }
}
