package com.wealthfront.magellan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.internal.test.DummyStep
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.transitions.NoAnimationTransition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class LegacyJourneyTest {

  private lateinit var activityController: ActivityController<FakeActivity>
  private lateinit var context: Context
  private lateinit var screenContainer: ScreenContainer
  private lateinit var navigator: Navigator
  private lateinit var journey: LegacyJourney<*>

  @Before
  fun setUp() {
    activityController = Robolectric.buildActivity(FakeActivity::class.java)
    context = activityController.get()
    screenContainer = ScreenContainer(context)
    navigator = Navigator(container = { screenContainer })
    journey = DummyJourney().apply { navigator = this@LegacyJourneyTest.navigator }
  }

  @Test
  fun givenEmptyBackStack_currentNavigable_isSelf() {
    assertThat(journey.currentNavigable).isEqualTo(journey)
  }

  @Test
  fun givenSingleLeafBackStack_currentNavigable_isThatLeaf() {
    val singleLeaf = DummyStep()
    navigator.set(singleLeaf)
    assertThat(journey.currentNavigable).isEqualTo(singleLeaf)
  }

  @Test
  fun givenSingleBranchBackStack_currentNavigable_isTopLeaf() {
    val topLeaf = DummyStep()
    val singleBranch = DummyJourney().apply {
      navigator = Navigator(container = { screenContainer }).apply {
        set(topLeaf)
      }
    }
    navigator.set(singleBranch)
    assertThat(journey.currentNavigable).isEqualTo(topLeaf)
  }

  @Test
  fun givenMultiBranchBackStack_currentNavigable_isTopLeaf() {
    val topLeaf = DummyStep()
    val bottomBranch = DummyJourney().apply {
      navigator = Navigator(container = { screenContainer }).apply {
        set(DummyStep())
      }
    }
    val nestedBranch = DummyJourney().apply {
      navigator = Navigator(container = { screenContainer }).apply {
        set(topLeaf)
      }
    }
    val multiBranch = DummyJourney().apply {
      navigator = Navigator(container = { screenContainer }).apply {
        set(nestedBranch)
      }
    }
    navigator.navigate(Direction.FORWARD) { backStack ->
      backStack.clear()
      val transition = NoAnimationTransition()
      backStack.push(NavigationEvent(bottomBranch, transition))
      backStack.push(NavigationEvent(multiBranch, transition))
      backStack.first()
    }
    assertThat(journey.currentNavigable).isEqualTo(topLeaf)
  }

  @Test
  fun givenCustomNavigable_currentNavigable_defersToCustomNavigable() {
    val customLeaf = DummyStep()
    val customNavigable = Mockito.mock(NavigableCompat::class.java)
    `when`(customNavigable.currentNavigable).thenReturn(customLeaf)
    navigator.set(customNavigable)
    assertThat(journey.currentNavigable).isEqualTo(customLeaf)
  }

  private open class FakeActivity : AppCompatActivity()

  private open class DummyJourney :
    LegacyJourney<MagellanDummyLayoutBinding>(
      MagellanDummyLayoutBinding::inflate,
      MagellanDummyLayoutBinding::container
    )

  fun Navigator.set(navigable: NavigableCompat) {
    navigate(Direction.BACKWARD) { backStack ->
      backStack.clear()
      NavigationEvent(navigable, NoAnimationTransition()).run {
        backStack.push(this)
        this
      }
    }
  }
}
