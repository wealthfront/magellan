package com.wealthfront.magellan.core

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater.from
import android.view.View
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.internal.test.DummyStep
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.lifecycle.LifecycleState.Created
import com.wealthfront.magellan.lifecycle.LifecycleState.Destroyed
import com.wealthfront.magellan.lifecycle.LifecycleState.Shown
import com.wealthfront.magellan.lifecycle.transitionToState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
internal class StepTest {

  private lateinit var context: Activity
  private lateinit var step: DummyStep

  @Captor lateinit var sparseArrayCaptor: ArgumentCaptor<SparseArray<Parcelable>>
  @Mock lateinit var view: View
  private var transitionFinished = false

  @Before
  fun setUp() {
    initMocks(this)
    transitionFinished = false
    step = DummyStep { transitionFinished = true }
    step.view = view
    context = buildActivity(Activity::class.java).get()
    step.viewBinding = MagellanDummyLayoutBinding.inflate(from(context))
  }

  @Test
  fun onShow() {
    step.transitionToState(Shown(context))

    verify(view, never()).restoreHierarchyState(any())
  }

  @Test
  fun onHide() {
    step.transitionToState(Shown(context))
    step.transitionToState(Created(context))

    verify(view).saveHierarchyState(any())
  }

  @Test
  fun saveRestore() {
    val state = Bundle()
    state.putString("key", "value")
    doAnswer { sparseArrayCaptor.value.put(42, state) }.`when`(view).saveHierarchyState(sparseArrayCaptor.capture())

    step.transitionToState(Shown(context))
    step.transitionToState(Created(context))
    verify(view).saveHierarchyState(any())

    step.transitionToState(Destroyed)
    step.transitionToState(Shown(context))
    verify(view).restoreHierarchyState(sparseArrayCaptor.capture())

    val bundle = sparseArrayCaptor.value.get(42) as Bundle
    assertThat(bundle.getString("key")).isEqualTo("value")
  }

  @Test
  fun whenTransitionFinished() {
    step.transitionStarted()
    step.transitionToState(Shown(context))
    assertThat(transitionFinished).isFalse()
    step.transitionFinished()
    assertThat(transitionFinished).isTrue()

    step.transitionToState(Created(context))
    transitionFinished = false

    step.transitionStarted()
    step.transitionToState(Shown(context))
    step.transitionFinished()
    assertThat(transitionFinished).isTrue()
  }

  @Test
  fun whenTransitionFinished_beforeTransitionStarted() {
    step.transitionToState(Shown(context))
    step.transitionFinished()
    assertThat(transitionFinished).isTrue()

    transitionFinished = false
    step.transitionStarted()
    step.transitionFinished()
    assertThat(transitionFinished).isFalse()
  }

  @Test
  fun whenTransitionFinished_afterTransitionFinished() {
    step.transitionStarted()
    step.transitionFinished()
    step.transitionToState(Shown(context))
    assertThat(transitionFinished).isTrue()
  }
}
