package com.wealthfront.magellan.core

import android.app.Activity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.internal.test.InstrumentedStep
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transition
import com.wealthfront.magellan.lifecycle.transitionToState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
internal class ViewStateRestorationTest {

  private lateinit var context: Activity

  @Before
  fun setup() {
    context = buildActivity(Activity::class.java).get()
  }

  @Test
  fun onShow_viewStateRestoredExactlyOnceForStep() {
    val step = InstrumentedStep()
    step.transitionToState(LifecycleState.Shown(context))
    step.transitionToState(LifecycleState.Created(context))
    step.transitionToState(LifecycleState.Shown(context))

    assertThat(step.viewBinding!!.instrumented.onRestoreInstanceStateCount).isEqualTo(1)
  }

  @Test
  fun onShow_viewStateRestoredExactlyOnceForStepInJourney() {
    val step = InstrumentedStep()
    val journey = SimpleJourney()
    journey.transitionToState(LifecycleState.Shown(context))
    journey.navigator.goTo(step)

    journey.transitionToState(LifecycleState.Created(context))
    journey.transitionToState(LifecycleState.Shown(context))
    assertThat(step.viewBinding!!.instrumented.onRestoreInstanceStateCount).isEqualTo(1)
  }
}
