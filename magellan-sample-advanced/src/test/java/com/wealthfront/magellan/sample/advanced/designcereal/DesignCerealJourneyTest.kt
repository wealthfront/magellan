package com.wealthfront.magellan.sample.advanced.designcereal

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.transitionToState
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DesignCerealJourneyTest {

  private var isComplete: Boolean = false
  private val journey = DesignCerealJourney { isComplete = true }

  @Test
  fun onCreate() {
    journey.transitionToState(Created(getApplicationContext()))
    journey.navigator.backStack.let { backstack ->
      assertThat(backstack.size).isEqualTo(1)
      assertThat(backstack[0].navigable).isInstanceOf(DesignCerealPiecesStep::class.java)
    }

    journey.onPiecesSelected(CerealPieceType.CORN_FLAKE)
    journey.navigator.backStack.let { backstack ->
      assertThat(backstack.size).isEqualTo(2)
      assertThat(backstack[0].navigable).isInstanceOf(DesignCerealStyleStep::class.java)
      assertThat(backstack[1].navigable).isInstanceOf(DesignCerealPiecesStep::class.java)
    }

    journey.onStyleSelected(CerealPieceStyle.PLAIN, CerealPieceColor.NATURAL)
    journey.navigator.backStack.let { backstack ->
      assertThat(journey.navigator.backStack.size).isEqualTo(3)
      assertThat(backstack[0].navigable).isInstanceOf(DesignCerealCompleteStep::class.java)
      assertThat(backstack[1].navigable).isInstanceOf(DesignCerealStyleStep::class.java)
      assertThat(backstack[2].navigable).isInstanceOf(DesignCerealPiecesStep::class.java)
    }
  }

  @Test
  fun piecesSelected() {
  }
}