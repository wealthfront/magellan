package com.wealthfront.magellan.view

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Looper
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.DialogCreator
import com.wealthfront.magellan.lifecycle.LifecycleState
import com.wealthfront.magellan.lifecycle.transition
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
internal class DialogComponentTest {

  lateinit var dialog1: Dialog
  lateinit var dialog2: Dialog

  private val context = Robolectric.buildActivity(Activity::class.java).get()
  private val dialogComponent = DialogComponent()

  private lateinit var dialogCreator1: DialogCreator
  private lateinit var dialogCreator2: DialogCreator

  @Before
  fun setUp() {
    initMocks(this)
    dialogCreator1 = DialogCreator {
      AlertDialog.Builder(context).create().also { dialog1 = it }
    }
    dialogCreator2 = DialogCreator {
      AlertDialog.Builder(context).create().also { dialog2 = it }
    }
  }

  @Test
  fun showDialog() {
    dialogComponent.transition(LifecycleState.Created(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Resumed(context))
    dialogComponent.showDialog(dialogCreator1)
    assertThat(dialog1.isShowing).isTrue()
  }

  @Test
  fun showDialog_manuallyDismissed() {
    dialogComponent.transition(LifecycleState.Created(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Resumed(context))
    dialogComponent.showDialog(dialogCreator1)
    assertThat(dialog1.isShowing).isTrue()

    dialog1.dismiss()
    dialogComponent.transition(LifecycleState.Resumed(context), LifecycleState.Shown(context))
    assertThat(dialogComponent.shouldRestoreDialog).isFalse()
  }

  @Test
  fun showDialog_rotation() {
    dialogComponent.transition(LifecycleState.Created(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Resumed(context))
    dialogComponent.showDialog(dialogCreator1)
    assertThat(dialog1.isShowing).isTrue()

    dialogComponent.transition(LifecycleState.Resumed(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Created(context))
    assertThat(dialog1.isShowing).isFalse()
    assertThat(dialogComponent.shouldRestoreDialog).isTrue()

    dialogComponent.transition(LifecycleState.Created(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Resumed(context))
    assertThat(dialog1.isShowing).isTrue()
  }

  @Test
  fun showDialog_diffDialog() {
    dialogComponent.transition(LifecycleState.Created(context), LifecycleState.Shown(context))
    dialogComponent.transition(LifecycleState.Shown(context), LifecycleState.Resumed(context))
    dialogComponent.showDialog(dialogCreator1)
    assertThat(dialog1.isShowing).isTrue()

    dialogComponent.showDialog(dialogCreator2)
    Shadows.shadowOf(Looper.getMainLooper()).idle()
    assertThat(dialog2.isShowing).isTrue()

    assertThat(dialogComponent.shouldRestoreDialog).isFalse()
    dialogComponent.transition(LifecycleState.Resumed(context), LifecycleState.Shown(context))
    assertThat(dialogComponent.shouldRestoreDialog).isTrue()
  }
}
