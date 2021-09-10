package com.wealthfront.magellan.lifecycle

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.AttachingActivity
import com.wealthfront.magellan.core.DummyStep
import com.wealthfront.magellan.core.Step
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.initMocks

@RunWith(AndroidJUnit4::class)
public class ActivityLifecycleAdapterTest {

  private lateinit var step: Step<*>

  private lateinit var activityScenario1: ActivityScenario<AttachingActivity>
  private lateinit var activityScenario2: ActivityScenario<AttachingActivity>

  @Before
  public fun setUp() {
    initMocks(this)
    step = DummyStep()
  }

  @After
  public fun tearDown() {
    activityScenario1.close()
    activityScenario2.close()
  }

  @Test
  public fun overlappingActivities_doesNotCrash() {
    var rule2Activity: Activity? = null
    AttachingActivity.step = step
    activityScenario1 = launchActivity()
    activityScenario2 = launchActivity()
    activityScenario2.onActivity { activity ->
      rule2Activity = activity
    }

    activityScenario1.moveToState(Lifecycle.State.RESUMED)
    activityScenario1.moveToState(Lifecycle.State.STARTED)
    activityScenario2.moveToState(Lifecycle.State.RESUMED)
    assertThat(step.currentState).isEqualTo(LifecycleState.Resumed(rule2Activity!!))
  }
}