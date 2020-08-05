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
class DialogComponentTest {

  @Mock lateinit var dialog1: Dialog
  @Mock lateinit var dialog2: Dialog

  private val context = Activity()
  private val dialogComponent = DialogComponent()

  private lateinit var dialogCreator1: DialogCreator
  private lateinit var dialogCreator2: DialogCreator

  @Before
  fun setUp() {
    initMocks(this)
    dialogCreator1 = DialogCreator { context -> dialog1 }
    dialogCreator2 = DialogCreator { context -> dialog2 }
  }

  @Test
  fun showDialog() {
    `when`(dialog1.isShowing).thenReturn(true)

    dialogComponent.create(context)
    dialogComponent.showDialog(DialogCreator { context -> dialogCreator1.createDialog(context) })

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }

  @Test
  fun showDialog_hidden() {
    `when`(dialog1.isShowing).thenReturn(true)

    dialogComponent.create(context)
    dialogComponent.showDialog(DialogCreator { context -> dialogCreator1.createDialog(context) })

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    `when`(dialog1.isShowing).thenReturn(false)
    dialogComponent.hide(context)

    verify(dialog1).dismiss()
    assertThat(dialogComponent.dialogIsShowing).isFalse()
  }

  @Test
  fun showDialog_rotation() {
    `when`(dialog1.isShowing).thenReturn(true)
    dialogComponent.create(context)
    dialogComponent.showDialog(DialogCreator { context -> dialogCreator1.createDialog(context) })

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    dialogComponent.hide(context)
    dialogComponent.destroy(context)

    `when`(dialog1.isShowing).thenReturn(false)
    dialogComponent.create(context)
    dialogComponent.show(context)

    verify(dialog1).dismiss()
    verify(dialog1, times(2)).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }

  @Test
  fun showDialog_diffDialog() {
    `when`(dialog1.isShowing).thenReturn(true)
    `when`(dialog2.isShowing).thenReturn(true)

    dialogComponent.create(context)
    dialogComponent.showDialog(DialogCreator { context -> dialogCreator1.createDialog(context) })

    verify(dialog1).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()

    dialogComponent.showDialog(DialogCreator { context -> dialogCreator2.createDialog(context) })

    verify(dialog2).show()
    assertThat(dialogComponent.dialogIsShowing).isTrue()
  }
}
