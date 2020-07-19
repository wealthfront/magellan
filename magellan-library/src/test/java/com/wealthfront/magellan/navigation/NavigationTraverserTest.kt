package com.wealthfront.magellan.navigation

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application

@RunWith(RobolectricTestRunner::class)
class NavigationTraverserTest {

  private lateinit var traverser: NavigationTraverser
  private lateinit var oneStepRoot: Journey<*>
  private lateinit var multiStepRoot: Journey<*>
  private lateinit var siblingRoot: Journey<*>
  private lateinit var journey1: Journey<*>
  private lateinit var step1: Step<*>
  private lateinit var step2: Step<*>
  private lateinit var step3: Step<*>
  private lateinit var journey2: Journey<*>
  private lateinit var step4: Step<*>
  private lateinit var journey3: Journey<*>

  @Before
  fun setUp() {
    oneStepRoot = RootJourney()
    multiStepRoot = MultiStepJourney()
    siblingRoot = SiblingJourney()
    journey1 = DummyJourney1()
    journey2 = DummyJourney2()
    journey3 = DummyJourney3((siblingRoot as SiblingJourney)::goToSiblingJourney)
    step1 = DummyStep1()
    step2 = DummyStep2()
    step3 = DummyStep3()
    step4 = DummyStep4()
  }

  @Test
  fun globalBackStackWithoutAnyNavigation() {
    traverser = NavigationTraverser(oneStepRoot)

    assertThat(traverser.getGlobalBackStack()).isEmpty()
  }

  @Test
  fun globalBackStackWithOneStep() {
    traverser = NavigationTraverser(oneStepRoot)

    oneStepRoot.create(application)

    assertThat(traverser.getGlobalBackStack()).isEqualTo(listOf(journey1, step1))
  }

  @Test
  fun globalBackStackWithMultipleStep() {
    traverser = NavigationTraverser(multiStepRoot)

    multiStepRoot.create(application)

    assertThat(traverser.getGlobalBackStack()).isEqualTo(listOf(journey2, step1, step2))
  }

  @Test
  fun globalBackStackWithSiblingJourney() {
    traverser = NavigationTraverser(siblingRoot)

    siblingRoot.create(application)

    assertThat(traverser.getGlobalBackStack()).isEqualTo(
      listOf(
        journey3,
        step3,
        step4,
        journey2,
        step1,
        step2))
  }

  private inner class RootJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey1)
    }
  }

  private inner class MultiStepJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey2)
    }
  }

  private inner class SiblingJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey3)
    }

    fun goToSiblingJourney() {
      navigator.goTo(journey2)
    }
  }

  private inner class DummyJourney1 : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(step1)
    }
  }

  private inner class DummyJourney2 : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(step1)
      navigator.goTo(step2)
    }
  }

  private inner class DummyJourney3(
    private val goToSiblingJourney: () -> Unit
  ) : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container) {

    override fun onCreate(context: Context) {
      navigator.goTo(step3)
      navigator.goTo(step4)
      goToSiblingJourney()
    }
  }

  private inner class DummyStep1 :
    Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

  private inner class DummyStep2 :
    Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

  private inner class DummyStep3 :
    Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

  private inner class DummyStep4 :
    Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
}
