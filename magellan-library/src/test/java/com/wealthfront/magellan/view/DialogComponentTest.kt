package com.wealthfront.magellan.view

import android.app.Activity
import android.app.Dialog
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.DialogCreator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DialogComponentTest {

  @Mock lateinit var dialog1: Dialog
  @Mock lateinit var dialog2: Dialog

  private val context = Activity()
  private val dialogComponent = DialogComponent()

  private lateinit var dialogCreator1: DialogCreator
  private lateinit var dialogCreator2: DialogCreator

  @Before
  fun setUp() {
    initMocks(this)
    dialogCreator1 = DialogCreator { dialog1 }
    dialogCreator2 = DialogCreator { dialog2 }
  }

  @Test
  fun showDialog() {
    `when`(dialog1.isShowing).thenReturn(true)

    dialogComponent.start(context)
    dialogComponent.resume(context)
    dialogComponent.showDialog(dialogCreator1)

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }

  @Test
  fun showDialog_hidden() {
    `when`(dialog1.isShowing).thenReturn(true)

    dialogComponent.start(context)
    dialogComponent.resume(context)
    dialogComponent.showDialog(dialogCreator1)

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    `when`(dialog1.isShowing).thenReturn(false)
    dialogComponent.pause(context)
    dialogComponent.stop(context)

    verify(dialog1).dismiss()
    assertThat(dialogComponent.dialogIsShowing).isFalse()
  }

  @Test
  fun showDialog_rotation() {
    `when`(dialog1.isShowing).thenReturn(true)
    dialogComponent.start(context)
    dialogComponent.resume(context)
    dialogComponent.showDialog(dialogCreator1)

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    dialogComponent.pause(context)
    dialogComponent.stop(context)
    dialogComponent.destroy(context)

    `when`(dialog1.isShowing).thenReturn(false)
    dialogComponent.create(context)
    dialogComponent.start(context)
    dialogComponent.resume(context)

    verify(dialog1).dismiss()
    verify(dialog1, times(2)).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }

  @Test
  fun showDialog_diffDialog() {
    `when`(dialog1.isShowing).thenReturn(true)
    `when`(dialog2.isShowing).thenReturn(true)

    dialogComponent.start(context)
    dialogComponent.resume(context)
    dialogComponent.showDialog(dialogCreator1)

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    dialogComponent.showDialog(dialogCreator2)

    verify(dialog2).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }
}
