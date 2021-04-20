package com.wealthfront.magellan.core

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater.from
import android.view.View
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
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
  private lateinit var screen: DummyStep

  @Captor lateinit var sparseArrayCaptor: ArgumentCaptor<SparseArray<Parcelable>>
  @Mock lateinit var view: View

  @Before
  fun setUp() {
    initMocks(this)
    screen = DummyStep()
    screen.view = view
    context = buildActivity(Activity::class.java).get()
    screen.viewBinding = MagellanDummyLayoutBinding.inflate(from(context))
  }

  @Test
  fun onShow() {
    screen.show(context)

    verify(view, never()).restoreHierarchyState(any())
  }

  @Test
  fun onHide() {
    screen.hide(context)

    verify(view).saveHierarchyState(any())
  }

  @Test
  fun saveRestore() {
    val state = Bundle()
    state.putString("key", "value")
    doAnswer { sparseArrayCaptor.value.put(42, state) }.`when`(view).saveHierarchyState(sparseArrayCaptor.capture())

    screen.hide(context)
    verify(view).saveHierarchyState(any())

    screen.show(context)
    verify(view).restoreHierarchyState(sparseArrayCaptor.capture())

    val bundle = sparseArrayCaptor.value.get(42) as Bundle
    assertThat(bundle.getString("key")).isEqualTo("value")
  }
}

internal class DummyStep : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
