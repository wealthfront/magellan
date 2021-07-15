package com.wealthfront.magellan.lifecycle

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.core.DummyStep
import com.wealthfront.magellan.core.Step
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations.initMocks

public class ActivityLifecycleAdapterTest : TestCase() {

  private lateinit var step: Step<*>

  @Rule
  public var rule: ActivityScenarioRule<AttachingActivity> = ActivityScenarioRule(AttachingActivity::class.java)
  @Rule
  public var rule2: ActivityScenarioRule<AttachingActivity> = ActivityScenarioRule(AttachingActivity::class.java)

  public override fun setUp() {
    initMocks(this)
    step = DummyStep()
  }

  @Test
  public fun overlappingActivities_doesNotCrash() {
    var rule2Activity: Activity? = null
    rule.scenario.onActivity { activity -> activity.step = step }
    rule2.scenario.onActivity { activity ->
      activity.step = step
      rule2Activity = activity
    }
    rule.scenario.moveToState(Lifecycle.State.RESUMED)
    rule.scenario.moveToState(Lifecycle.State.STARTED)
    rule2.scenario.moveToState(Lifecycle.State.RESUMED)
    assertThat(step.currentState).isEqualTo(LifecycleState.Resumed(rule2Activity!!))
  }
}

public class AttachingActivity : ComponentActivity() {
  internal lateinit var step: Step<*>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentScreen(step)
  }
}
