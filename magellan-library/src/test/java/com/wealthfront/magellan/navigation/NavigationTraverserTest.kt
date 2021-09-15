package com.wealthfront.magellan.navigation

import android.app.Activity
import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.test.databinding.MagellanDummyLayoutBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigationTraverserTest {

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
  private lateinit var journey3: DummyJourney3
  private lateinit var context: Activity

  @Before
  fun setUp() {
    context = buildActivity(Activity::class.java).get()
    oneStepRoot = RootJourney()
    multiStepRoot = MultiStepJourney()
    siblingRoot = SiblingJourney()
    journey1 = DummyJourney1()
    journey2 = DummyJourney2()
    journey3 = DummyJourney3((siblingRoot as SiblingJourney)::goToJourney2)
    step1 = DummyStep1()
    step2 = DummyStep2()
    step3 = DummyStep3()
    step4 = DummyStep4()
  }

  @Test
  fun globalBackStackWithOneStep() {
    traverser = NavigationTraverser(oneStepRoot)

    oneStepRoot.transitionToState(Created(context))

    assertThat(traverser.getGlobalBackstackDescription()).isEqualTo(
      """
    RootJourney
    └ DummyJourney1
      └ DummyStep1
    
      """.trimIndent()
    )
  }

  @Test
  fun globalBackStackWithMultipleStep() {
    traverser = NavigationTraverser(multiStepRoot)

    multiStepRoot.transitionToState(Created(context))

    assertThat(traverser.getGlobalBackstackDescription()).isEqualTo(
      """
    MultiStepJourney
    └ DummyJourney2
      ├ DummyStep1
      └ DummyStep2
    
      """.trimIndent()
    )
  }

  @Test
  fun globalBackStackWithSiblingJourney() {
    traverser = NavigationTraverser(siblingRoot)

    siblingRoot.transitionToState(Created(context))
    journey3.goToAnotherJourney()

    assertThat(traverser.getGlobalBackstackDescription()).isEqualTo(
      """
    SiblingJourney
    ├ DummyJourney3
    | ├ DummyStep3
    | └ DummyStep4
    └ DummyJourney2
      ├ DummyStep1
      └ DummyStep2
    
      """.trimIndent()
    )
  }

  private inner class RootJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey1)
    }
  }

  private inner class MultiStepJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey2)
    }
  }

  private inner class SiblingJourney : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(journey3)
    }

    fun goToJourney2() {
      navigator.goTo(journey2)
    }
  }

  private inner class DummyJourney1 : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(step1)
    }
  }

  private inner class DummyJourney2 : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(step1)
      navigator.goTo(step2)
    }
  }

  private inner class DummyJourney3(
    private val goToOtherJourney: () -> Unit
  ) : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  ) {

    override fun onCreate(context: Context) {
      navigator.goTo(step3)
      navigator.goTo(step4)
    }

    fun goToAnotherJourney() = goToOtherJourney()
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
