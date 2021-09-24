package com.wealthfront.magellan.sample.advanced.cerealcollection

import android.app.Application
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transitionToState
import com.wealthfront.magellan.sample.advanced.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CerealDetailStepTest {

  private val details = CerealDetails(R.string.cornflakes_title, R.string.cornflakes_description, CerealStatus.ACTIVE)
  private val detailStep = CerealDetailStep(details)

  @Test
  fun onShow() {
    val context = getApplicationContext<Application>()
    detailStep.transitionToState(Created(context))
    detailStep.transitionToState(Shown(context))
    with(detailStep.viewBinding!!) {
      assertThat(cerealTitle.text).isEqualTo("Cornflakes")
      assertThat(cerealDescription.text).isEqualTo("These flakes epitomise the simplistic diets advocated by their inventor, J.H. Kellogg.")
      assertThat(cerealStatus.text).isEqualTo("Active")
    }
  }
}